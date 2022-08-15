package com.rsicarelli.zeroglu.presentation.recipedetail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
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
    verticalArrangement: Arrangement.HorizontalOrVertical = Arrangement.spacedBy(40.dp),
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
private fun Instructions(instructionsItem: List<InstructionItem>) {
    instructionsItem.forEachIndexed { index, instructionItems ->
        if (index > 0) {
            Spacer(modifier = Modifier.height(16.dp))
        }

        Column {
            Text(
                fontWeight = FontWeight.Medium,
                fontStyle = FontStyle.Italic,
                text = stringResource(id = R.string.instructions),
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
    ingredients: List<IngredientItem>,
) {
    val tableItems by remember {
        derivedStateOf {
            ingredients.map {
                TableItem(
                    it.value.first,
                    it.value.second
                )
            }
        }
    }

    TableContainer(
        ingredients = tableItems,
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
                TableItem(
                    "Frame:",
                    breadShapes
                ),
                TableItem(
                    "Color:",
                    setupItem.browningLevel.value
                ),
                TableItem(
                    "Menu:",
                    setupItem.programme.value.toString()
                ),
                TableItem(
                    "Time:",
                    "${TimeUnit.MILLISECONDS.toMinutes(totalTime ?: 0)} minutes"
                ),
            )
        }
    }

    TableContainer(
        modifier = modifier,
        ingredients = tableItems,
        title = stringResource(id = R.string.setup)
    )
}

@Immutable
data class TableItem(
    val title: String,
    val description: String,
)

@Composable
private fun TableContainer(
    modifier: Modifier = Modifier,
    title: String,
    ingredients: List<TableItem>,
    titleWeight: Float = 0.22F,
    subtitleWeight: Float = 0.77F,
) {
    ExpandableContainer(
        modifier = modifier,
        title = title
    ) {
        Column {
            ingredients.asSequence().forEach { ingredientItem ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        modifier = Modifier.weight(titleWeight),
                        text = ingredientItem.title,
                        textAlign = TextAlign.End,
                        maxLines = 1,
                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
                        overflow = TextOverflow.Ellipsis,
                    )

                    Text(
                        text = ingredientItem.description,
                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
                        textAlign = TextAlign.Start,
                        modifier = Modifier.weight(subtitleWeight),
                    )
                }
            }
        }
    }
}

@Composable
fun ExpandableContainer(
    modifier: Modifier = Modifier,
    title: String,
    content: @Composable () -> Unit,
) {
    var isExpanded by rememberSaveable { mutableStateOf(true) }

    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .clickable { isExpanded = !isExpanded }
    ) {
        val (titleRef, collapseRef, animatedContentRef) = createRefs()

        Text(
            modifier = Modifier.constrainAs(titleRef) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(collapseRef.start)
                width = Dimension.fillToConstraints
            },
            fontWeight = FontWeight.Medium,
            fontStyle = FontStyle.Italic,
            text = title,
            style = MaterialTheme.typography.titleLarge
        )

        ExpandCollapseButton(
            isExpanded = isExpanded,
            onClick = { isExpanded = !isExpanded },
            modifier = Modifier
                .size(50.dp)
                .constrainAs(collapseRef) {
                    end.linkTo(parent.end)
                    top.linkTo(titleRef.top)
                    bottom.linkTo(titleRef.bottom)
                }
        )

        AnimatedVisibility(
            modifier = Modifier.constrainAs(animatedContentRef) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(titleRef.bottom)
            },
            visible = isExpanded,
            enter = expandVertically() + fadeIn(initialAlpha = 0.2F),
            exit = shrinkVertically() + fadeOut(targetAlpha = 0.0f)
        ) {
            content()
        }
    }
}

@Composable
private fun ExpandCollapseButton(
    isExpanded: Boolean,
    modifier: Modifier,
    onClick: () -> Unit,
    initialRotation: Float = 180F,
) {
    var currentRotation by remember { mutableStateOf(initialRotation) }
    val rotation = remember { Animatable(currentRotation) }

    LaunchedEffect(isExpanded) {
        if (isExpanded) {
            rotation.animateTo(
                targetValue = 180F,
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearEasing
                )
            ) {
                currentRotation = value
            }
        } else {
            if (currentRotation > 0f) {
                rotation.animateTo(
                    targetValue = 0F,
                    animationSpec = tween(
                        durationMillis = 600,
                        easing = LinearOutSlowInEasing
                    )
                ) {
                    currentRotation = value
                }
            }
        }
    }

    IconButton(
        modifier = modifier.then(Modifier.size(48.dp)),
        onClick = onClick,
    ) {
        Icon(
            modifier = Modifier.rotate(currentRotation),
            painter = painterResource(id = R.drawable.ic_round_expand_more_24),
            contentDescription = if (isExpanded) {
                stringResource(id = R.string.collapse)
            } else {
                stringResource(id = R.string.expand)
            },
        )
    }
}
