package com.rsicarelli.zeroglu_recipes.feature

import android.icu.text.CaseMap
import android.widget.Space
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ramcosta.composedestinations.annotation.Destination
import com.rsicarelli.zeroglu_recipes.R
import com.rsicarelli.zeroglu_recipes.domain.model.Ingredient
import com.rsicarelli.zeroglu_recipes.domain.model.Recipe

@Destination
@Composable
fun RecipeDetailScreen(recipe: Recipe) {

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(40.dp)
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Title(recipe.title)
                SettingsContainer(recipe)
            }
        }

        item { IngredientsContainer(recipe.ingredients) }

        item {
            recipe.instructions.filter { it.steps.any { a -> a.isNotEmpty() } }
                .forEachIndexed { index, instruction ->
                    if (index > 0) {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    Column {
                        Text(
                            fontWeight = FontWeight.Medium,
                            fontStyle = FontStyle.Italic,
                            text = instruction.custom_title ?: "Method",
                            style = MaterialTheme.typography.titleLarge
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        instruction.steps.filter { it.isNotEmpty() }
                            .forEachIndexed { index, step ->
                                Text(
                                    text = "${index + 1}. $step",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                    }
                }
        }
    }
}

@Composable
private fun IngredientsContainer(ingredients: List<Ingredient>) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        ingredients.map { ingredient ->

            Text(
                fontWeight = FontWeight.Medium,
                fontStyle = FontStyle.Italic,
                text = ingredient.custom_title?.ifBlank { "Ingredients" } ?: "Ingredients",
                style = MaterialTheme.typography.titleLarge
            )

            val valueAndDescription = ingredient.items
                .map { it.splitToSequence(":") }
                .map { Pair(it.first().trim(), it.last().trim()) }

            Column {
                valueAndDescription.forEach {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(
                            modifier = Modifier.weight(0.22F),
                            text = it.first,
                            textAlign = TextAlign.End,
                            maxLines = 1,
                            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
                            overflow = TextOverflow.Ellipsis,
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = it.second,
                            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
                            textAlign = TextAlign.Start,
                            modifier = Modifier.weight(0.77f),
                        )
                    }
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
private fun SettingsContainer(recipe: Recipe) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        recipe.setup.forEach { setup ->
            setup.custom_title?.let {
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
                setup.bread_shapes.filter { it.isNotEmpty() }.forEach { shape ->
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
                        id = when (setup.browning_level) {
                            "low" -> R.drawable.ic_browning_low
                            "medium" -> R.drawable.ic_browning_medium
                            "high" -> R.drawable.ic_browning_high
                            else -> -1
                        }
                    ),
                    contentDescription = setup.browning_level
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
                        text = setup.programme.toString(),
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
