package com.rsicarelli.zeroglu.presentation.recipedetail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.rsicarelli.zeroglu.R

@Composable
fun ExpandableContainer(
    modifier: Modifier = Modifier,
    title: String,
    buttonSize: Dp = 50.dp,
    topTitlePadding: Dp = 8.dp,
    enterTransition: EnterTransition = expandVertically() + fadeIn(initialAlpha = 0.2F),
    exitTransition: ExitTransition = shrinkVertically() + fadeOut(targetAlpha = 0.0f),
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
                top.linkTo(parent.top, topTitlePadding)
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
                .size(buttonSize)
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
            enter = enterTransition,
            exit = exitTransition
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
    size: Dp = 48.dp,
    enterAnimationTime: Int = 300,
    exitAnimationTime: Int = 600,
) {
    var currentRotation by remember { mutableStateOf(initialRotation) }
    val rotation = remember { Animatable(currentRotation) }

    LaunchedEffect(isExpanded) {
        val (targetRotation, animationTime, easing) =
            if (isExpanded)
                Triple(initialRotation, enterAnimationTime, LinearEasing)
            else Triple(0F, exitAnimationTime, LinearOutSlowInEasing)

        rotation.animateTo(targetRotation, tween(animationTime, easing = easing)) {
            currentRotation = value
        }
    }

    IconButton(
        modifier = modifier.then(Modifier.size(size)),
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
