package com.rsicarelli.zeroglu.presentation.recipedetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

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
    titleWeight: Float = 0.25F,
    subtitleWeight: Float = 0.75F,
) {
    ExpandableContainer(
        modifier = modifier,
        title = title
    ) {
        Column {
            tableItems.asSequence().forEach { tableItem ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        modifier = Modifier
                            .weight(titleWeight)
                            .align(Alignment.Top),
                        text = tableItem.title,
                        textAlign = TextAlign.End,
                        maxLines = 1,
                        style = MaterialTheme.typography.bodyLarge,
                        overflow = TextOverflow.Ellipsis,
                    )

                    Text(
                        modifier = Modifier
                            .weight(subtitleWeight)
                            .align(Alignment.CenterVertically),
                        text = tableItem.description,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Start,
                    )
                }
            }
        }
    }
}
