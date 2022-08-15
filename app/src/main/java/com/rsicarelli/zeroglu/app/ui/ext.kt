package com.rsicarelli.zeroglu.app.ui

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.shimmer

@Composable
fun Modifier.defaultPlaceholder(
    isLoading: Boolean,
    color: Color = MaterialTheme.colorScheme.surface,
    highlightColor: Color = MaterialTheme.colorScheme.surfaceVariant,
): Modifier = composed {
    placeholder(
        color = color,
        visible = isLoading,
        shape = CircleShape,
        highlight = PlaceholderHighlight.shimmer(
            highlightColor = highlightColor,
        )
    )
}


