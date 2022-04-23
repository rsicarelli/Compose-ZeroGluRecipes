@file:OptIn(ExperimentalMaterial3Api::class)

package com.rsicarelli.zeroglu_recipes.feature.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.rsicarelli.zeroglu_recipes.data.RecipeRemoteDataSource
import com.rsicarelli.zeroglu_recipes.domain.model.Recipe
import com.rsicarelli.zeroglu_recipes.domain.model.Tag
import com.rsicarelli.zeroglu_recipes.feature.destinations.RecipeDetailScreenDestination
import com.rsicarelli.zeroglu_recipes.feature.home.components.ChipGroup

@Destination(start = true)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(
        factory = HomeViewModelFactory(RecipeRemoteDataSource())
    ),
    navigator: DestinationsNavigator
) {
    val recipes by viewModel.recipes.collectAsState()
    val tags by viewModel.tags.collectAsState()
    val selectedTags by viewModel.selectedTags.collectAsState()

    Content(tags, selectedTags, viewModel, recipes, navigator)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Content(
    tags: List<Tag>,
    selectedTags: Set<Tag>,
    viewModel: HomeViewModel,
    recipes: List<Recipe>,
    navigator: DestinationsNavigator
) {
    LazyColumn(
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        content = {
            stickyHeader {
                Header(tags, selectedTags, viewModel)
            }

            items(
                count = recipes.size,
                key = { recipes[it].index }
            ) {
                RecipeItem(recipes, it, navigator)
            }
        })
}

@Composable
private fun Header(
    tags: List<Tag>,
    selectedTags: Set<Tag>,
    viewModel: HomeViewModel
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        ChipGroup(
            modifier = Modifier.background(MaterialTheme.colorScheme.surface),
            items = tags,
            selectedItems = selectedTags.toList(),
            onSelectedChanged = { viewModel.onTagSelected(it) },
            chipName = { it.description["en"] ?: "" }
        )
    }
}

@Composable
private fun RecipeItem(
    recipes: List<Recipe>,
    it: Int,
    navigator: DestinationsNavigator
) {
    val recipe = recipes[it]
    Card(
        onClick = { navigator.navigate(RecipeDetailScreenDestination(recipe)) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(32.dp)
    ) {
        Text(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 24.dp, top = 16.dp, bottom = 16.dp, end = 16.dp),
            text = "${recipe.index}. ${recipe.title}",
            fontSize = 16.sp,
        )
    }
}

class HomeViewModelFactory(private val recipeDataSource: RecipeRemoteDataSource) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(recipeDataSource) as T
        }

        throw IllegalStateException()
    }

}