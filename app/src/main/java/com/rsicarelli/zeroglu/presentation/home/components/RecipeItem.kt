package com.rsicarelli.zeroglu.presentation.home.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.rsicarelli.zeroglu.presentation.home.RecipeItem
import com.rsicarelli.zeroglu.presentation.home.TagItem
import com.rsicarelli.zeroglu.presentation.home.components.RecipeItemDefaults.CardShape
import com.rsicarelli.zeroglu.presentation.home.components.RecipeItemDefaults.DefaultArrangement
import com.rsicarelli.zeroglu.presentation.home.components.RecipeItemDefaults.HorizontalPadding
import com.rsicarelli.zeroglu.presentation.home.components.RecipeItemDefaults.VerticalPadding

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
    val HorizontalPadding = 24.dp

    @Stable
    val VerticalPadding = 16.dp

    @Stable
    val DefaultArrangement = Arrangement.spacedBy(8.dp)
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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = HorizontalPadding)
                .then(Modifier.padding(vertical = VerticalPadding)),
            verticalArrangement = DefaultArrangement
        ) {

            Text(
                text = recipe.title.trim(),
                style = MaterialTheme.typography.headlineMedium,
            )

            LazyRow(
                horizontalArrangement = DefaultArrangement
            ) {
                items(
                    items = recipe.tags.values.toList(),
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
private fun RecipeItemChip(
    modifier: Modifier = Modifier,
    verticalPadding: Dp = 8.dp,
    horizontalPadding: Dp = 12.dp,
    tagName: String,
    borderStroke: BorderStroke = BorderStroke(1.dp, colorScheme.onSurface.copy(alpha = 0.15F)),
) {
    Box(
        modifier = modifier.border(borderStroke, MaterialTheme.shapes.extraLarge),
    ) {
        Text(
            modifier = Modifier
                .padding(horizontal = horizontalPadding)
                .then(Modifier.padding(vertical = verticalPadding)),
            text = tagName,
            style = MaterialTheme.typography.labelMedium
        )
    }
}
