package com.rsicarelli.zeroglu.presentation.home

import android.util.Log
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rsicarelli.zeroglu.data.RecipeRemoteDataSource
import com.rsicarelli.zeroglu.domain.model.Recipe
import com.rsicarelli.zeroglu.domain.model.Tag
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext

@Stable
class HomeViewModel(
    recipeRemoteDataSource: RecipeRemoteDataSource = RecipeRemoteDataSource.instance,
) : ViewModel() {

    private val selectedTagItems = MutableStateFlow<Sequence<TagItem>>(emptySequence())

    val state: StateFlow<HomeState> =
        combine(
            flow = recipeRemoteDataSource.recipes,
            flow2 = recipeRemoteDataSource.tags,
            flow3 = selectedTagItems,
            transform = ::HomeState
        ).catch {
            emit(HomeState(errorItem = UnknownError))
                .also { Log.e("HomeViewModel", "Something wrong is not right $it") }
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            HomeState(errorItem = UnknownError)
        )

    private suspend fun HomeState(
        recipeItems: List<Recipe>,
        tags: List<Tag>,
        selectedTags: Sequence<TagItem>,
        coroutineContext: CoroutineContext = Dispatchers.Default,
    ): HomeState = supervisorScope {
        HomeState(
            recipeItems = withContext(coroutineContext) { recipeItems.toRecipeItems() },
            tags = withContext(coroutineContext) { tags.toTagsItem() },
            selectedTags = selectedTags,
        )
    }

    internal fun onTagSelected(tagItem: TagItem) = run {
        viewModelScope.launch(Dispatchers.Default) {
            selectedTagItems.value
                .toMutableList()
                .apply {
                    if (contains(tagItem))
                        remove(tagItem)
                    else add(tagItem)
                }
                .asSequence()
                .let { selectedTagItems.value = it }
        }
    }
}
