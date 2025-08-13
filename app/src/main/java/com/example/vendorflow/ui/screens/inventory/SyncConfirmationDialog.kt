package com.example.vendorflow.ui.screens.inventory

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vendorflow.data.enums.SyncSource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SyncConfirmationDialog(
    onInventoryEvent: (InventoryEvent) -> Unit,
    inventoryState: InventoryState,
    modifier: Modifier = Modifier
) {
    BasicAlertDialog(
        onDismissRequest = {
            onInventoryEvent(InventoryEvent.HideConfirmationDialog)
        },
        modifier = modifier
    ) {
        Surface(
            shape = RoundedCornerShape(size = 20.dp),
//            border = BorderStroke(
//                width = 1.dp,
//                color = MaterialTheme.colorScheme.primary
//            )
        ) {
            Column(
                modifier = Modifier
                    .padding(all = 12.dp),
                verticalArrangement = Arrangement.spacedBy(space = 10.dp)
            ) {
                Text(
                    text = "Are you sure?",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 36.sp
                )
                Text(
                    text = buildAnnotatedString {
                        append("This will override your data in ")
                        when (inventoryState.syncSource) {
                            SyncSource.VENDOR_FLOW -> append("Notion")
                            SyncSource.NOTION -> append("Vendor Flow")
                        }
                        append(".")
                    }
                )
                Row (
                    modifier = Modifier
                        .align(Alignment.End),
                    horizontalArrangement = Arrangement.spacedBy(space = 10.dp)
                ) {
                    Button(
                        onClick = {
                            onInventoryEvent(InventoryEvent.HideConfirmationDialog)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.background,
                            contentColor = MaterialTheme.colorScheme.primary,
                        ),
                        border = BorderStroke(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text(
                            text = "Cancel"
                        )
                    }
                    Button(
                        onClick = {
                            Log.i("SyncConfirmationDialog.kt", "Syncing...")
                            when (inventoryState.syncSource) {
                                SyncSource.VENDOR_FLOW -> onInventoryEvent(InventoryEvent.UpdateNotion)
                                SyncSource.NOTION -> onInventoryEvent(InventoryEvent.UpdateVendorFlow)
                            }
                            onInventoryEvent(InventoryEvent.HideConfirmationDialog)
                        }
                    ) {
                        Text(
                            text = "Confirm"
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun SyncConfirmationDialogPreview() {
    SyncConfirmationDialog(
        onInventoryEvent = { },
        inventoryState = InventoryState(),
    )
}