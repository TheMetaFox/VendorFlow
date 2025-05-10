package com.example.vendorflow.ui.screens.catalog

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ImageSearch
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.vendorflow.ui.theme.VendorFlowTheme

@Composable
fun CatalogItemDialog(
    modifier: Modifier = Modifier,
    onCatalogEvent: (CatalogEvent) -> Unit,
    catalogState: CatalogState,
    ) {
    Dialog(
        onDismissRequest = {
            onCatalogEvent(CatalogEvent.HideCatalogItemDialog)
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
                val context = LocalContext.current
                val photoPickerLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.PickVisualMedia(),
                    onResult = { uri ->
                        if (uri == null) return@rememberLauncherForActivityResult
                        onCatalogEvent(CatalogEvent.UpdateImageField(uri))
                    }
                )
                Box(
                    modifier = Modifier
                        .size(100.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        imageVector = Icons.Outlined.ImageSearch,
                        contentDescription = null,
                        modifier = Modifier
                            .size(80.dp),
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                    )
                    AsyncImage(
                        model = catalogState.productImageUri,
                        contentDescription = "Product image",
                        modifier = modifier
                            .clickable {
                                photoPickerLauncher.launch(
                                    input = PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            },
                        contentScale = ContentScale.Crop
                    )
                }
                TextField(
                    value = catalogState.productNameField,
                    onValueChange = {
                        onCatalogEvent(
                            CatalogEvent.UpdateTextField(
                                textField = "Name",
                                text = it
                            )
                        )
                    },
                    label = { Text(text = "Name") },
                    placeholder = { Text(text = "") },
                )
                TextField(
                    value = catalogState.collectionField,
                    onValueChange = {
                        onCatalogEvent(
                            CatalogEvent.UpdateTextField(
                                textField = "Collection",
                                text = it
                            )
                        )
                    },
                    label = { Text(text = "Collection") },
                    placeholder = { Text(text = "") },
                )
                TextField(
                    value = catalogState.priceField,
                    onValueChange = {
                        onCatalogEvent(
                            CatalogEvent.UpdateTextField(
                                textField = "Price",
                                text = it
                            )
                        )
                    },
                    label = { Text(text = "Price") },
                    placeholder = { Text(text = "") },
                )
                TextField(
                    value = catalogState.costField,
                    onValueChange = {
                        onCatalogEvent(
                            CatalogEvent.UpdateTextField(
                                textField = "Cost",
                                text = it
                            )
                        )
                    },
                    label = { Text(text = "Cost") },
                    placeholder = { Text(text = "") },
                )
                Button(
                    onClick = {
                        onCatalogEvent(CatalogEvent.UpsertCatalogItem(context = context))
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
fun CatalogItemDialogPreview() {
    VendorFlowTheme {
        CatalogItemDialog(
            modifier = Modifier
                .width(width = 300.dp),
            onCatalogEvent = { },
            catalogState = CatalogState()
        )
    }
}