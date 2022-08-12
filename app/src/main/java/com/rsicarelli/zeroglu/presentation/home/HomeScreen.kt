@file:OptIn(ExperimentalMaterial3Api::class)

package com.rsicarelli.zeroglu.presentation.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.rsicarelli.zeroglu.data.RecipeRemoteDataSource
import com.rsicarelli.zeroglu.presentation.destinations.RecipeDetailScreenDestination
import com.rsicarelli.zeroglu.presentation.home.components.ChipGroup

@OptIn(ExperimentalLifecycleComposeApi::class)
@RootNavGraph(start = true)
@Destination
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(
        factory = HomeViewModelFactory(RecipeRemoteDataSource.instance)
    ),
    navigator: DestinationsNavigator,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    if (state.recipeItems.isNotEmpty()) {
        HomeContent(
            tags = state.tags,
            selectedTags = state.selectedTags,
            recipes = state.recipeItems,
            onTagSelected = viewModel::onTagSelected,
            navigator = navigator,
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HomeContent(
    tags: ComposeLazyList<TagItem>,
    selectedTags: Sequence<TagItem>,
    recipes: ComposeLazyList<RecipeItem>,
    onTagSelected: (TagItem) -> Unit,
    navigator: DestinationsNavigator,
) {
    var recipeToNavigate by remember { mutableStateOf<RecipeItem?>(null) }

    LaunchedEffect(
        key1 = recipeToNavigate != null,
        block = {
            recipeToNavigate?.let { recipeItem ->
                navigator.navigate(RecipeDetailScreenDestination(recipeItem))
            }
        }
    )

    LazyColumn(
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        content = {
            stickyHeader {
                TagsStickyHeader(
                    tags = tags,
                    selectedTags = selectedTags,
                    onTagSelected = onTagSelected
                )
            }

            items(
                count = recipes.count(),
                key = { index ->
                    recipes[index + 1.toLong()]?.index!!
                }
            ) { index ->
                val recipeItem by remember { derivedStateOf { requireNotNull(recipes[index + 1.toLong()]) } }

                RecipeItem(
                    recipe = recipeItem,
                    onNavigateToDetail = { recipeToNavigate = recipeItem }
                )
            }
        }
    )
}

@Composable
private fun TagsStickyHeader(
    modifier: Modifier = Modifier,
    tags: ComposeLazyList<TagItem>,
    selectedTags: Sequence<TagItem>,
    onTagSelected: (TagItem) -> Unit,
) {
    ChipGroup(
        modifier = modifier.background(MaterialTheme.colorScheme.surface),
        items = tags,
        selectedItems = selectedTags,
        onSelectedChanged = onTagSelected,
        chipName = TagItem::description
    )
}

@Composable
private fun RecipeItem(
    recipe: RecipeItem,
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

            val tags by derivedStateOf {
                recipe.tags.filterNot { (_, tagItem) ->
                    with(tagItem) {
                        description.contains("Loaf") ||
                            description.contains("Pizza") ||
                            description.contains("Ciabatta") ||
                            description.contains("Roll")
                    }
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
                items(
                    items = tags.values.toList(),
                    key = TagItem::id
                ) { tagItem ->
                    RecipeItemChip(
                        tagName = tagItem.description
                    )
                }
            }
        }
    }
}

@Composable
private fun ConstraintLayoutScope.IndexTitle(
    indexRef: ConstrainedLayoutReference,
    index: String,
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
private fun RecipeItemChip(tagName: String) {
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
            text = tagName,
            style = MaterialTheme.typography.labelMedium
        )
    }
}

class HomeViewModelFactory(private val recipeDataSource: RecipeRemoteDataSource) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(recipeDataSource) as T
        }

        throw IllegalStateException()
    }

}
