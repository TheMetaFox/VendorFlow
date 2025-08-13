package com.example.vendorflow.ui.screens.tags

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.vendorflow.ui.theme.VendorFlowTheme

@Composable
fun TagDialog(
    modifier: Modifier = Modifier,
    onTagsEvent: (TagsEvent) -> Unit,
    tagsState: TagsState,
) {
    Dialog(
        onDismissRequest = {
            onTagsEvent(TagsEvent.HideTagDialog)
        },
    ) {
        Box(
            modifier = modifier
                .padding(all = 10.dp)
        ) {
            Column(
                modifier = modifier,
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(
                    value = tagsState.tagNameField,
                    onValueChange = {
                        onTagsEvent(
                            TagsEvent.UpdateTagNameField(
                                text = it
                            )
                        )
                    },
                    label = { Text(text = "Name") },
                    placeholder = { Text(text = "") },
                )
                Button(
                    onClick = {
                        onTagsEvent(TagsEvent.UpsertTag)
                    },
                    modifier = Modifier
                        .size(width = 250.dp, height = 65.dp)
                ) {
                    Text(
                        text = "Save",
                        fontSize = 35.sp
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun TagDialogPreview() {
    VendorFlowTheme {
        TagDialog(
            modifier = Modifier
                .size(width = 300.dp, height = 200.dp),
            onTagsEvent = { },
            tagsState = TagsState()
        )
    }
}