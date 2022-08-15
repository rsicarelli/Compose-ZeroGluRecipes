package com.rsicarelli.zeroglu.presentation.recipedetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.rsicarelli.zeroglu.R
import com.rsicarelli.zeroglu.presentation.home.IngredientItem
import com.rsicarelli.zeroglu.presentation.home.InstructionItem
import com.rsicarelli.zeroglu.presentation.home.RecipeItem
import com.rsicarelli.zeroglu.presentation.home.SetupItem
import java.util.concurrent.TimeUnit

@Destination
@Composable
fun RecipeDetailScreen(recipeItem: RecipeItem) {
    RecipeDetailContent(recipeItem = recipeItem)
}

@Composable
private fun RecipeDetailContent(
    modifier: Modifier = Modifier,
    recipeItem: RecipeItem,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    verticalArrangement: Arrangement.HorizontalOrVertical = Arrangement.spacedBy(20.dp),
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding,
        verticalArrangement = verticalArrangement
    ) {
        item(key = recipeItem.title) { Title(title = recipeItem.title) }
        item(key = recipeItem.setup) { SettingsContainer(setupItem = recipeItem.setup.first(), totalTime = recipeItem.totalTimeMillis) }
        item(key = recipeItem.ingredients) { IngredientsContainer(ingredients = recipeItem.ingredients) }
        item(key = recipeItem.instructions) { Instructions(instructionsItem = recipeItem.instructions) }
    }
}

@Composable
private fun Instructions(
    instructionsItem: List<InstructionItem>,
    spacer: @Composable () -> Unit = {
        Spacer(modifier = Modifier.height(16.dp))
    },
) {
    ExpandableContainer(title = stringResource(id = R.string.instructions)) {
        instructionsItem.forEachIndexed { index, instructionItems ->
            if (index > 0) spacer()

            Column {
                if (index > 0) {
                    Text(
                        fontWeight = FontWeight.Medium,
                        fontStyle = FontStyle.Italic,
                        text = stringResource(id = R.string.part_at, index.toString()),
                        style = MaterialTheme.typography.titleLarge
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                }

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
}

@Composable
private fun IngredientsContainer(
    ingredients: List<IngredientItem>,
) {
    val tableItems by remember {
        derivedStateOf(ingredients.asSequence()
            .map {
                TableItem(
                    it.value.first,
                    it.value.second
                )
            }.sortedByDescending { tableItem ->
                runCatching {
                    tableItem.title.replace("g", "").toInt()
                }.getOrElse {
                    tableItem.title.length
                }
            }::toList
        )
    }

    TableContainer(
        tableItems = tableItems,
        title = stringResource(id = R.string.ingredients)
    )
}

@Composable
private fun Title(title: String) {
    Text(
        text = title,
        fontWeight = FontWeight.Medium,
        style = MaterialTheme.typography.headlineMedium
    )
}

@Composable
private fun SettingsContainer(
    modifier: Modifier = Modifier,
    setupItem: SetupItem,
    totalTime: Long?,
) {
    val breadShapes by remember {
        derivedStateOf {
            setupItem.breadShapes.value.filter { breadShape -> breadShape.value.isNotEmpty() }
                .joinToString(", ") { it.value }
        }
    }

    val tableItems by remember {
        derivedStateOf {
            listOf(
                TableItem("Frame:", breadShapes),
                TableItem("Color:", setupItem.browningLevel.value),
                TableItem("Menu:", setupItem.programme.value.toString()),
                TableItem("Time:", "${TimeUnit.MILLISECONDS.toMinutes(totalTime ?: 0)} minutes"),
            )
        }
    }

    TableContainer(
        modifier = modifier,
        tableItems = tableItems,
        title = stringResource(id = R.string.setup)
    )
}
