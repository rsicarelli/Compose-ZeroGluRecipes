package com.rsicarelli.zeroglu.presentation.recipedetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Immutable
data class TableItem(
    val title: String,
    val description: String,
)

@Composable
fun TableContainer(
    modifier: Modifier = Modifier,
    title: String,
    tableItems: List<TableItem>,
    titleWeight: Float = 0.20F,
    subtitleWeight: Float = 0.80F,
) {
    ExpandableContainer(
        modifier = modifier,
        title = title
    ) {
        Column {
            tableItems.asSequence().forEach { ingredientItem ->
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
