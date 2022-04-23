package com.rsicarelli.zeroglu_recipes.feature.home

import android.system.Os.remove
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rsicarelli.zeroglu_recipes.data.RecipeRemoteDataSource
import com.rsicarelli.zeroglu_recipes.domain.model.Recipe
import com.rsicarelli.zeroglu_recipes.domain.model.Tag
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
    recipeRemoteDataSource: RecipeRemoteDataSource
) : ViewModel() {

    init {
        viewModelScope.launch { recipeRemoteDataSource.init().collect() }
    }

    private val _selectedTags = MutableStateFlow(setOf<Tag>())
    val selectedTags: StateFlow<Set<Tag>> = _selectedTags.asStateFlow()

    val recipes: StateFlow<List<Recipe>> =
        combine(
            recipeRemoteDataSource.recipes,
            selectedTags
        ) { recipes, selectedTags ->
            if (selectedTags.isNotEmpty()) {
                recipes.filter {
                    (it.tags).containsAll(selectedTags)
                }
            } else {
                recipes
            }
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    val tags: StateFlow<List<Tag>> = recipeRemoteDataSource.tags.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    fun onTagSelected(tag: Tag) {
        val list = if (_selectedTags.value.contains(tag)) {
            _selectedTags.value.toMutableSet().apply { remove(tag) }
        } else {
            _selectedTags.value.toMutableSet().apply { add(tag) }
        }
        _selectedTags.tryEmit(list)
    }

}