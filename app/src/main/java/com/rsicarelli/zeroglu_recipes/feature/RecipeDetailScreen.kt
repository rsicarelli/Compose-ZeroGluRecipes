package com.rsicarelli.zeroglu_recipes.feature

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.common.collect.Table
import com.ramcosta.composedestinations.annotation.Destination
import com.rsicarelli.zeroglu_recipes.R
import com.rsicarelli.zeroglu_recipes.domain.model.Recipe
import kotlinx.serialization.json.JsonNull.content

@Destination
@Composable
fun RecipeDetailScreen(recipe: Recipe) {

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
    ) {

        item {

        }

        item {
            Text(
                text = recipe.title,
                fontWeight = FontWeight.Medium,
                style = MaterialTheme.typography.headlineMedium
            )
        }

        item {
            Spacer(modifier = Modifier.height(12.dp))
        }

        item {
            recipe.ingredients.filter { it.items.any { a -> a.isNotEmpty() } }
                .forEachIndexed { index, ingredient ->
                    if (index > 0) {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    Column {
                        Text(
                            fontWeight = FontWeight.Medium,
                            fontStyle = FontStyle.Italic,
                            text = ingredient.customTitle ?: "Ingredients",
                            style = MaterialTheme.typography.titleLarge
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        ingredient.items.filter { it.isNotEmpty() }.map { it.replace(":", "of") }
                            .forEach {
                                Text(text = it, style = MaterialTheme.typography.bodyLarge)
                            }
                    }
                }
        }

        item {
            Spacer(modifier = Modifier.height(12.dp))
        }

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
                            text = instruction.customTitle ?: "Method",
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
fun <T> Table(
    columnCount: Int,
    cellWidth: (index: Int) -> Dp,
    data: List<T>,
    modifier: Modifier = Modifier,
    headerCellContent: @Composable (index: Int) -> Unit,
    cellContent: @Composable (index: Int, item: T) -> Unit,
) {
    LazyRow(
        modifier = Modifier.padding(16.dp)
    ) {
        items((0 until columnCount).toList()) { columnIndex ->
            Column {
                (0..data.size).forEach { index ->
                    Surface(
                        border = BorderStroke(1.dp, Color.LightGray),
                        contentColor = Color.Transparent,
                        modifier = Modifier.width(cellWidth(columnIndex))
                    ) {
                        if (index != 0) {
                            cellContent(columnIndex, data[index - 1])
                        }
                    }
                }
            }
        }
    }
}
