package com.rsicarelli.zeroglu.presentation.home.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.rsicarelli.zeroglu.presentation.home.TagItem

@Composable
fun ChipGroup(
    modifier: Modifier,
    items: List<TagItem>,
    selectedItems: Sequence<TagItem>,
    chipName: (TagItem) -> String,
    onSelectedChanged: (TagItem) -> Unit,
) {
    Column(modifier = modifier.padding(8.dp)) {
        LazyRow {
            items(
                items = items,
                key = TagItem::id
            ) { chipItem ->
                Chip(
                    name = chipName(chipItem),
                    isSelected = selectedItems.contains(chipItem),
                    onSelectionChanged = {
                        onSelectedChanged(chipItem)
                    },
                )
            }
        }
    }
}

@Composable
fun Chip(
    name: String,
    isSelected: Boolean = false,
    onSelectionChanged: (String) -> Unit = {},
    selectedBackgroundColor: Color = MaterialTheme.colorScheme.primary,
) {

    val backgroundColor = if (isSelected) selectedBackgroundColor else Color.Unspecified
    Box(
        modifier = Modifier
            .padding(4.dp)
            .background(
                backgroundColor,
                MaterialTheme.shapes.large
            )
            .border(
                width = if (isSelected) 0.dp else 1.dp,
                color = if (isSelected) Color.Unspecified else MaterialTheme.colorScheme.onSurface.copy(
                    alpha = 0.7F
                ),
                shape = MaterialTheme.shapes.large
            )
            .clip(MaterialTheme.shapes.large),
    ) {
        Row(modifier = Modifier
            .toggleable(
                value = isSelected,
                onValueChange = {
                    onSelectionChanged(name)
                }
            )
            .padding(start = 8.dp, top = 8.dp, bottom = 8.dp, end = 16.dp)
            .requiredHeight(32.dp),
            verticalAlignment = Alignment.CenterVertically

        ) {
            AnimatedVisibility(
                visible = isSelected,
            ) {
                Icon(
                    modifier = Modifier.padding(start = 4.dp),
                    imageVector = Icons.Default.Check,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.contentColorFor(
                        backgroundColor
                    )
                )
            }
            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = name,
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.contentColorFor(
                        backgroundColor
                    )
                ),
            )
        }
    }
}
