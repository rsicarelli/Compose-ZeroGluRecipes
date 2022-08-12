package com.rsicarelli.zeroglu.presentation.recipedetail

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ramcosta.composedestinations.annotation.Destination
import com.rsicarelli.zeroglu.R
import com.rsicarelli.zeroglu.presentation.home.IngredientItem
import com.rsicarelli.zeroglu.presentation.home.InstructionItem
import com.rsicarelli.zeroglu.presentation.home.RecipeItem
import com.rsicarelli.zeroglu.presentation.home.SetupItem

@Destination
@Composable
fun RecipeDetailScreen(recipeItem: RecipeItem) {
    RecipeDetailContent(recipeItem = recipeItem)
}

@Composable
private fun RecipeDetailContent(
    modifier: Modifier = Modifier,
    recipeItem: RecipeItem,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(40.dp)
    ) {
        item(key = recipeItem.title) { Title(recipeItem.title) }
        item(key = recipeItem.setup) { SettingsContainer(recipeItem.setup) }
        item(key = recipeItem.ingredients) { IngredientsContainer(recipeItem.ingredients) }
        item(key = recipeItem.instructions) { Instructions(recipeItem.instructions) }
    }
}

@Composable
private fun Instructions(instructionsItem: Sequence<InstructionItem>) {
    instructionsItem.forEachIndexed { index, instructionItems ->
        if (index > 0) {
            Spacer(modifier = Modifier.height(16.dp))
        }

        requireNotNull(instructionItems.title)

        Column {
            Text(
                fontWeight = FontWeight.Medium,
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                text = instructionItems.title,
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            instructionItems
                .steps
                .forEachIndexed { index, step ->
                    Text(
                        text = "${index + 1}. $step",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
        }
    }
}

@Composable
private fun IngredientsContainer(
    ingredients: Sequence<IngredientItem>,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            fontWeight = FontWeight.Medium,
            fontStyle = FontStyle.Italic,
            text = stringResource(id = R.string.ingredients),
            style = MaterialTheme.typography.titleLarge
        )

        Column {
            ingredients.asSequence().forEach { ingredientItem ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        modifier = Modifier.weight(0.22F),
                        text = ingredientItem.value.first,
                        textAlign = TextAlign.End,
                        maxLines = 1,
                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
                        overflow = TextOverflow.Ellipsis,
                    )

                    Text(
                        text = ingredientItem.value.second,
                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
                        textAlign = TextAlign.Start,
                        modifier = Modifier.weight(0.77f),
                    )
                }
            }
        }
    }
}

@Composable
private fun Title(recipe: String) {
    Text(
        text = recipe,
        fontWeight = FontWeight.Medium,
        style = MaterialTheme.typography.headlineMedium
    )
}

@Composable
private fun SettingsContainer(setupItems: Sequence<SetupItem>) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        setupItems.forEach { setupItem ->
            setupItem.title?.let {
                Text(
                    fontWeight = FontWeight.Medium,
                    fontStyle = FontStyle.Italic,
                    text = it,
                    style = MaterialTheme.typography.titleLarge
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                setupItem.breadShapes.value.filter { it.isNotEmpty() }.forEach { shape ->
                    Icon(
                        modifier = Modifier.size(48.dp),
                        painter = painterResource(
                            id = when (shape) {
                                "large" -> R.drawable.ic_shape_large
                                "medium" -> R.drawable.ic_shape_medium
                                "small" -> R.drawable.ic_shape_small
                                else -> 0
                            }
                        ),
                        contentDescription = shape
                    )
                }

                Icon(
                    modifier = Modifier.size(48.dp),
                    painter = painterResource(
                        id = when (setupItem.browningLevel.value) {
                            "low" -> R.drawable.ic_browning_low
                            "medium" -> R.drawable.ic_browning_medium
                            "high" -> R.drawable.ic_browning_high
                            else -> -1
                        }
                    ),
                    contentDescription = setupItem.browningLevel.value
                )

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .border(2.dp, MaterialTheme.colorScheme.onSurface, shape = CircleShape)
                        .layout { measurable, constraints ->
                            // Measure the composable
                            val placeable = measurable.measure(constraints)

                            //get the current max dimension to assign width=height
                            val currentHeight = placeable.height
                            var heightCircle = currentHeight
                            if (placeable.width > heightCircle)
                                heightCircle = placeable.width

                            //assign the dimension and the center position
                            layout(heightCircle, heightCircle) {
                                // Where the composable gets placed
                                placeable.placeRelative(
                                    0,
                                    (heightCircle - currentHeight) / 2
                                )
                            }
                        }) {

                    Text(
                        text = setupItem.programme.toString(),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Black,
                        modifier = Modifier
                            .padding(4.dp)
                            .defaultMinSize(35.dp) //Use a min size for short text.
                    )
                }
            }
        }
    }
}
