package com.rsicarelli.zeroglu.presentation.home.components

import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.rsicarelli.zeroglu.presentation.home.TagItem

@Composable
internal fun TagsStickyHeader(
    modifier: Modifier = Modifier,
    tags: List<TagItem>,
    selectedTags: Sequence<TagItem>,
    onTagSelected: (TagItem) -> Unit,
    isLoading: Boolean,
) {
    ChipGroup(
        modifier = modifier.background(MaterialTheme.colorScheme.surface),
        items = tags,
        isLoading = isLoading,
        selectedItems = selectedTags,
        onSelectedChanged = onTagSelected,
        chipName = TagItem::description
    )
}
