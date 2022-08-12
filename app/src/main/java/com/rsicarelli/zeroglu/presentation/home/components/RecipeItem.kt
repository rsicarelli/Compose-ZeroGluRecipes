package com.rsicarelli.zeroglu.presentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintLayoutBaseScope
import androidx.constraintlayout.compose.ConstraintLayoutScope
import androidx.constraintlayout.compose.Dimension
import com.rsicarelli.zeroglu.presentation.home.RecipeItem
import com.rsicarelli.zeroglu.presentation.home.TagItem
import com.rsicarelli.zeroglu.presentation.home.components.RecipeItemDefaults.CardPadding
import com.rsicarelli.zeroglu.presentation.home.components.RecipeItemDefaults.CardShape
import com.rsicarelli.zeroglu.presentation.home.components.RecipeItemDefaults.EndGuidelinePadding
import com.rsicarelli.zeroglu.ui.ComposeLazyList

@Composable
internal fun RecipeItem(
    recipe: RecipeItem,
    onNavigateToDetail: () -> Unit,
) = RecipeItemContent(
    onNavigateToDetail = onNavigateToDetail,
    recipe = recipe
)

private object RecipeItemDefaults {

    @Stable
    val CardShape = 32.dp

    @Stable
    val CardPadding = 16.dp

    @Stable
    val EndGuidelinePadding = 0.dp
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun RecipeItemContent(
    modifier: Modifier = Modifier,
    onNavigateToDetail: () -> Unit,
    recipe: RecipeItem,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        onClick = onNavigateToDetail,
        shape = RoundedCornerShape(CardShape)
    ) {
        ConstraintLayout(
            modifier = Modifier
                .padding(CardPadding)
                .fillMaxWidth()
        ) {
            val (indexRef, titleRef, tagsRef) = createRefs()
            val endGuideline = createGuidelineFromEnd(EndGuidelinePadding)

            IndexTitle(
                indexRef = indexRef,
                index = recipe.index.toString(),
                topReference = titleRef
            )

            RecipeTitle(
                ref = titleRef,
                anchorRef = indexRef,
                endGuideline = endGuideline,
                title = recipe.title
            )

            TagItem(
                ref = tagsRef,
                endGuideline = endGuideline,
                topAndStartRef = titleRef,
                tags = recipe.tags
            )
        }
    }
}

@Composable
private fun ConstraintLayoutScope.IndexTitle(
    modifier: Modifier = Modifier,
    indexRef: ConstrainedLayoutReference,
    topReference: ConstrainedLayoutReference,
    index: String,
    backgroundColor: Color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3F),
    shape: RoundedCornerShape = CircleShape,
    size: Dp = 50.dp,
    textStyle: TextStyle = MaterialTheme.typography.titleLarge.copy(
        color = MaterialTheme.colorScheme.contentColorFor(
            MaterialTheme.colorScheme.primary.copy(alpha = 0.3F)
        ),
        lineHeight = 2.5.em,
        platformStyle = PlatformTextStyle(
            includeFontPadding = false
        ),
        lineHeightStyle = LineHeightStyle(
            alignment = LineHeightStyle.Alignment.Center,
            trim = LineHeightStyle.Trim.Both
        )
    ),
) {
    Box(
        modifier = modifier
            .constrainAs(indexRef) {
                top.linkTo(topReference.top)
                start.linkTo(parent.start)
            }
            .size(size),
    ) {

        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .background(backgroundColor, shape)
                .fillMaxSize()
        )

        Text(
            modifier = Modifier.align(Alignment.Center),
            text = index,
            textAlign = TextAlign.Center,
            style = textStyle
        )
    }
}

@Composable
fun ConstraintLayoutScope.RecipeTitle(
    modifier: Modifier = Modifier,
    ref: ConstrainedLayoutReference,
    anchorRef: ConstrainedLayoutReference,
    endGuideline: ConstraintLayoutBaseScope.VerticalAnchor,
    startPadding: Dp = 8.dp,
    endPadding: Dp = 16.dp,
    title: String,
) = Text(
    modifier = modifier
        .constrainAs(ref) {
            top.linkTo(anchorRef.top)
            start.linkTo(anchorRef.end, startPadding)
            bottom.linkTo(anchorRef.bottom)
            end.linkTo(endGuideline, endPadding)
            width = Dimension.fillToConstraints
        },
    text = title.trim(),
    style = MaterialTheme.typography.headlineMedium,
)

@Composable
fun ConstraintLayoutScope.TagItem(
    ref: ConstrainedLayoutReference,
    endGuideline: ConstraintLayoutBaseScope.VerticalAnchor,
    topAndStartRef: ConstrainedLayoutReference,
    tags: ComposeLazyList<TagItem>,
    endPadding: Dp = 16.dp,
    topPadding: Dp = 8.dp,
    horizontalArrangement: Arrangement.HorizontalOrVertical = Arrangement.spacedBy(8.dp),
) {
    LazyRow(
        modifier = Modifier
            .constrainAs(ref) {
                end.linkTo(endGuideline, endPadding)
                width = Dimension.fillToConstraints
                top.linkTo(topAndStartRef.bottom, topPadding)
                start.linkTo(topAndStartRef.start)
            },
        horizontalArrangement = horizontalArrangement
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
