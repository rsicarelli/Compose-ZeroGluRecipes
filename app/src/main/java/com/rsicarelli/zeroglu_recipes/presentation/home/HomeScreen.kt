@file:OptIn(ExperimentalMaterial3Api::class)

package com.rsicarelli.zeroglu_recipes.presentation.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintLayoutScope
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.rsicarelli.zeroglu_recipes.data.RecipeRemoteDataSource
import com.rsicarelli.zeroglu_recipes.domain.model.Recipe
import com.rsicarelli.zeroglu_recipes.domain.model.Tag
import com.rsicarelli.zeroglu_recipes.presentation.destinations.RecipeDetailScreenDestination
import com.rsicarelli.zeroglu_recipes.presentation.home.components.ChipGroup

@Destination(start = true)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(
        factory = HomeViewModelFactory(RecipeRemoteDataSource.instance)
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
                val recipe = recipes[it]
                RecipeItem(
                    recipe = recipe,
                    onNavigateToDetail = {
                        navigator.navigate(RecipeDetailScreenDestination(recipe))
                    }
                )
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
    recipe: Recipe,
    onNavigateToDetail: () -> Unit,
) {
    Card(
        onClick = onNavigateToDetail,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(32.dp)
    ) {
        ConstraintLayout(
            Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            val (indexRef, titleRef, tagsRef) = createRefs()
            val rightGuideline = createGuidelineFromEnd(0.dp)

            IndexTitle(indexRef, recipe.index.toString())

            val tags = derivedStateOf {
                recipe.tags.filterNot {
                    it.description.containsValue("Loaf") ||
                            it.description.containsValue("Pizza") ||
                            it.description.containsValue("Ciabatta") ||
                            it.description.containsValue("Roll")
                }
            }

            Text(
                modifier = Modifier
                    .constrainAs(titleRef) {
                        top.linkTo(indexRef.top)
                        start.linkTo(indexRef.end, 8.dp)
                        bottom.linkTo(indexRef.bottom)
                        end.linkTo(rightGuideline, 16.dp)
                        width = Dimension.fillToConstraints
                    },
                text = recipe.title.trim(),
                style = MaterialTheme.typography.headlineMedium,
            )

            LazyRow(
                modifier = Modifier
                    .constrainAs(tagsRef) {
                        end.linkTo(rightGuideline, 16.dp)
                        width = Dimension.fillToConstraints
                        top.linkTo(titleRef.bottom, 8.dp)
                        start.linkTo(titleRef.start)
                    },
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(tags.value.size, key = { tags.value[it].id }) {
                    val tag = tags.value[it].description["en"].orEmpty()
                    RecipeItemChip(tag)
                }
            }
        }
    }
}

@Composable
private fun ConstraintLayoutScope.IndexTitle(
    indexRef: ConstrainedLayoutReference,
    index: String
) {
    Box(contentAlignment = Alignment.Center,
        modifier = Modifier
            .constrainAs(indexRef) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
            }
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.3F), shape = CircleShape)
            .layout { measurable, constraints ->
                val placeable = measurable.measure(constraints)

                val currentHeight = placeable.height
                var heightCircle = currentHeight
                if (placeable.width > heightCircle)
                    heightCircle = placeable.width

                layout(heightCircle, heightCircle) {
                    placeable.placeRelative(0, (heightCircle - currentHeight) / 2)
                }
            }) {

        Text(
            text = index,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge.copy(
                color = MaterialTheme.colorScheme.contentColorFor(
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.3F)
                )
            ),
            modifier = Modifier
                .padding(8.dp)
                .defaultMinSize(24.dp)
        )
    }
}

@Composable
private fun RecipeItemChip(tag: String) {
    Box(
        modifier = Modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onSurface.copy(
                    alpha = 0.15F
                ),
                shape = MaterialTheme.shapes.large
            )
            .clip(MaterialTheme.shapes.large),
    ) {
        Text(
            modifier = Modifier.padding(8.dp),
            text = tag,
            style = MaterialTheme.typography.labelMedium
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