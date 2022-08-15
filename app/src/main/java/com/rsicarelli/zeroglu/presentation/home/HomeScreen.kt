package com.rsicarelli.zeroglu.presentation.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.rsicarelli.zeroglu.presentation.destinations.RecipeDetailScreenDestination
import com.rsicarelli.zeroglu.presentation.home.HomeContentDefaults.DefaultContentPadding
import com.rsicarelli.zeroglu.presentation.home.HomeContentDefaults.DefaultVerticalArrangement
import com.rsicarelli.zeroglu.presentation.home.components.RecipeItem
import com.rsicarelli.zeroglu.presentation.home.components.TagsStickyHeader
import org.koin.androidx.compose.getViewModel

@RootNavGraph(start = true)
@Destination
@Composable
@OptIn(ExperimentalLifecycleComposeApi::class)
internal fun HomeScreen(
    viewModel: HomeViewModel = getViewModel(),
    navigator: DestinationsNavigator,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val onRecipeSelected: (RecipeItem) -> Unit = remember(navigator) {
        { navigator.navigate(RecipeDetailScreenDestination(it)) }
    }

    HomeContent(
        tags = state.tags,
        selectedTags = state.selectedTags,
        recipes = state.recipeItems,
        onTagSelected = viewModel::onTagSelected,
        onRecipeSelected = onRecipeSelected
    )
}

@Composable
@OptIn(ExperimentalFoundationApi::class)
private fun HomeContent(
    tags: List<TagItem>,
    selectedTags: Sequence<TagItem>,
    recipes: List<RecipeItem>,
    onTagSelected: (TagItem) -> Unit,
    onRecipeSelected: (RecipeItem) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.statusBarsPadding(),
        contentPadding = PaddingValues(DefaultContentPadding),
        verticalArrangement = Arrangement.spacedBy(DefaultVerticalArrangement),
        content = {
            stickyHeader(key = tags.hashCode()) {
                TagsStickyHeader(
                    tags = tags,
                    selectedTags = selectedTags,
                    onTagSelected = onTagSelected
                )
            }

            if (recipes.isEmpty()) {
                item { Text(text = "Nothing here") }
            } else {
                items(
                    items = recipes,
                    key = RecipeItem::index
                ) { recipeItem ->
                    val onNavigateToDetail: () -> Unit = remember(recipeItem) {
                        { onRecipeSelected(recipeItem) }
                    }
                    RecipeItem(
                        modifier = Modifier.animateItemPlacement(),
                        recipe = recipeItem,
                        onNavigateToDetail = onNavigateToDetail
                    )
                }
            }

        },
    )
}

private object HomeContentDefaults {

    @Stable
    val DefaultContentPadding = 8.dp

    @Stable
    val DefaultVerticalArrangement = 16.dp
}
