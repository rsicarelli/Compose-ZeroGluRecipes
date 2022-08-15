package com.rsicarelli.zeroglu.presentation.recipedetail

import android.icu.text.CaseMap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.ramcosta.composedestinations.annotation.Destination
import com.rsicarelli.zeroglu.R
import com.rsicarelli.zeroglu.presentation.home.IngredientItem
import com.rsicarelli.zeroglu.presentation.home.InstructionItem
import com.rsicarelli.zeroglu.presentation.home.RecipeItem
import com.rsicarelli.zeroglu.presentation.home.SetupItem
import java.util.concurrent.TimeUnit
import kotlin.math.min

@Destination
@Composable
fun RecipeDetailScreen(recipeItem: RecipeItem) {
    RecipeDetailContent(recipeItem = recipeItem)
}

@Composable
private fun RecipeDetailContent(
    modifier: Modifier = Modifier,
    recipeItem: RecipeItem,
    verticalArrangement: Arrangement.HorizontalOrVertical = Arrangement.spacedBy(20.dp),
) {
    val scrollState = rememberScrollState()

    Box(
        modifier = modifier.padding(bottom = 24.dp)
    ) {
        val painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data(BreadsBgs.random())
                .build()
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer {
                    alpha = min(1f, 1 - (scrollState.value / 600f))
                    translationY = -scrollState.value * 0.1f
                }

        ) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 250.dp),
                painter = painter,
                contentDescription = "bread",
                contentScale = ContentScale.Crop
            )

            //Gradient overlay
            Spacer(
                Modifier
                    .fillMaxWidth()
                    .height(32.dp)
                    .align(Alignment.BottomCenter)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                MaterialTheme.colorScheme.background
                            )
                        )
                    )
            )
        }

        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .padding(top = 200.dp, start = 16.dp, end = 16.dp),
            verticalArrangement = verticalArrangement,
        ) {
            Title(title = recipeItem.title)
            SettingsContainer(setupItem = recipeItem.setup.first(), totalTime = recipeItem.totalTimeMillis)
            IngredientsContainer(ingredients = recipeItem.ingredients)
            Instructions(instructionsItem = recipeItem.instructions)
        }
    }
}

@Composable
private fun Instructions(
    instructionsItem: List<InstructionItem>,
    spacer: @Composable () -> Unit = {
        Spacer(modifier = Modifier.height(8.dp))
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
                        if (index > 0)
                            Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                modifier = Modifier
                                    .align(Alignment.Top),
                                text = "${index + 1}",
                                textAlign = TextAlign.End,
                                maxLines = 1,
                                style = MaterialTheme.typography.bodyLarge,
                                overflow = TextOverflow.Ellipsis,
                            )

                            Text(
                                modifier = Modifier
                                    .weight(1F)
                                    .padding(top = 2.dp)
                                    .align(Alignment.CenterVertically),
                                text = step,
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Start,
                            )
                        }
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
        style = MaterialTheme.typography.displaySmall
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
                TableItem("Frame", breadShapes),
                TableItem("Color", setupItem.browningLevel.value),
                TableItem("Menu", setupItem.programme.value.toString()),
                TableItem("Time", "${TimeUnit.MILLISECONDS.toMinutes(totalTime ?: 0)} minutes"),
            )
        }
    }

    TableContainer(
        modifier = modifier,
        tableItems = tableItems,
        title = stringResource(id = R.string.setup)
    )
}

val BreadsBgs = listOf(
    R.drawable.bg_bread,
    R.drawable.bread_1,
    R.drawable.bread_2,
    R.drawable.bread_3,
    R.drawable.bread_4,
    R.drawable.bread_5,
    R.drawable.bread_6,
    R.drawable.bread_7,
    R.drawable.bread_10,
    R.drawable.bread_11,
    R.drawable.bread_12,
    R.drawable.bread_13,
    R.drawable.bread_14,
    R.drawable.bread_15,
    R.drawable.bread_16,
    R.drawable.bread_17,
    R.drawable.bread_18,
    R.drawable.bread_19,
)
