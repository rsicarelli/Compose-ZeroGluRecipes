@file:OptIn(ExperimentalMaterial3Api::class)

package com.rsicarelli.zeroglu.presentation.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.rsicarelli.zeroglu.presentation.destinations.RecipeDetailScreenDestination
import com.rsicarelli.zeroglu.presentation.home.HomeContentDefaults.DefaultContentPadding
import com.rsicarelli.zeroglu.presentation.home.HomeContentDefaults.DefaultVerticalArrangement

@OptIn(ExperimentalLifecycleComposeApi::class)
@RootNavGraph(start = true)
@Destination
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = remember(::HomeViewModel),
    navigator: DestinationsNavigator,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val onRecipeSelected: (RecipeItem) -> Unit = remember(navigator) {
        { navigator.navigate(RecipeDetailScreenDestination(it)) }
    }

    if (state.recipeItems.isNotEmpty()) {
        HomeContent(
            tags = state.tags,
            selectedTags = state.selectedTags,
            recipes = state.recipeItems,
            onTagSelected = viewModel::onTagSelected,
            onRecipeSelected = onRecipeSelected
        )
    } else {
        //show empty state
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HomeContent(
    tags: ComposeLazyList<TagItem>,
    selectedTags: Sequence<TagItem>,
    recipes: ComposeLazyList<RecipeItem>,
    onTagSelected: (TagItem) -> Unit,
    onRecipeSelected: (RecipeItem) -> Unit,
) {
    LazyColumn(
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

            items(
                items = recipes.values.toList(),
                key = RecipeItem::index
            ) { recipeItem ->
                val onNavigateToDetail: () -> Unit = remember(recipeItem) {
                    { onRecipeSelected(recipeItem) }
                }
                RecipeItem(
                    recipe = recipeItem,
                    onNavigateToDetail = onNavigateToDetail
                )
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
