package com.rsicarelli.zeroglu_recipes.feature.home.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun <T> ChipGroup(
    items: List<T>,
    selectedCar: List<T> = listOf(),
    chipName: (T) -> String,
    onSelectedChanged: (T) -> Unit = {},
) {
    Column(modifier = Modifier.padding(8.dp)) {
        LazyRow {
            items(items.size) {
                val item = items[it]
                Chip(
                    name = chipName(item),
                    isSelected = selectedCar.contains(item),
                    onSelectionChanged = {
                        onSelectedChanged(item)
                    },
                )
            }
        }
    }
}

@Composable
fun Chip(
    name: String = "Chip",
    isSelected: Boolean = false,
    onSelectionChanged: (String) -> Unit = {},
    selectedBackgroundColor: Color = MaterialTheme.colorScheme.primary
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
                enter = expandHorizontally(expandFrom = Alignment.Start),
                exit = shrinkHorizontally(shrinkTowards = Alignment.Start)
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