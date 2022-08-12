package com.rsicarelli.zeroglu.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun RecipeItem(
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

@Composable private fun ConstraintLayoutScope.IndexTitle(
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
