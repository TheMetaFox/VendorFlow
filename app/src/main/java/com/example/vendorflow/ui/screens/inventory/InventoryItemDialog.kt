package com.example.vendorflow.ui.screens.inventory

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ImageSearch
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
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
import com.example.vendorflow.data.room.entities.Tag
import com.example.vendorflow.ui.theme.VendorFlowTheme

@Composable
fun InventoryItemDialog(
    modifier: Modifier = Modifier,
    onInventoryEvent: (InventoryEvent) -> Unit,
    inventoryState: InventoryState,
    ) {
    Dialog(
        onDismissRequest = {
            onInventoryEvent(InventoryEvent.HideProductDialog)
        },
    ) {
        Box(
            modifier = modifier
                .padding(all = 10.dp)
        ) {
            Column(
                modifier = modifier,
                verticalArrangement = Arrangement.spacedBy(space = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val context = LocalContext.current
                val photoPickerLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.PickVisualMedia(),
                    onResult = { uri ->
                        if (uri == null) return@rememberLauncherForActivityResult
                        onInventoryEvent(InventoryEvent.UpdateImageField(uri))
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
                        model = inventoryState.productImageUri,
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
                    value = inventoryState.productNameField,
                    onValueChange = {
                        onInventoryEvent(
                            InventoryEvent.UpdateTextField(
                                textField = "Name",
                                text = it
                            )
                        )
                    },
                    label = { Text(text = "Name") },
                    placeholder = { Text(text = "") },
                )
                TextField(
                    value = inventoryState.priceField,
                    onValueChange = {
                        onInventoryEvent(
                            InventoryEvent.UpdateTextField(
                                textField = "Price",
                                text = it
                            )
                        )
                    },
                    label = { Text(text = "Price") },
                    placeholder = { Text(text = "") },
                )
                TextField(
                    value = inventoryState.costField,
                    onValueChange = {
                        onInventoryEvent(
                            InventoryEvent.UpdateTextField(
                                textField = "Cost",
                                text = it
                            )
                        )
                    },
                    label = { Text(text = "Cost") },
                    placeholder = { Text(text = "") },
                )

                LazyHorizontalStaggeredGrid(
                    rows = StaggeredGridCells.Adaptive(minSize = 34.dp),
                    modifier = Modifier
                        .fillMaxHeight(0.4f)
                        .fillMaxWidth(),
//                    contentPadding = PaddingValues(all = 5.dp),
                    verticalArrangement = Arrangement.spacedBy(space = 5.dp),
                    horizontalItemSpacing = 5.dp
                ) {
                    items(count = inventoryState.tagList.size) {
                        val tag: Tag = inventoryState.tagList[it]
                        FilterChip(
                            selected = (inventoryState.selectedTags.contains(element = tag)),
                            onClick = {
                                onInventoryEvent(InventoryEvent.UpdateSelectedTags(tag = tag))
                            },
                            label = {
                                Text(text = tag.tagName)
                            },
                            modifier = Modifier
                                .align(alignment = Alignment.CenterHorizontally),
                            colors = FilterChipDefaults.filterChipColors().copy(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f)
                            )
                        )
                    }
                }
                Button(
                    onClick = {
                        onInventoryEvent(InventoryEvent.UpsertProductItem(context = context))
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

val tag1 = Tag(
    tagId = 1,
    tagName = "Sanrio",
    ordinal = 1
)
val tag2 = Tag(
    tagId = 2,
    tagName = "Astrology",
    ordinal = 2
)
val tag3 = Tag(
    tagId = 3,
    tagName = "Necklace",
    ordinal = 3
)

@Preview(showBackground = true)
@Composable
fun CatalogItemDialogPreview() {
    VendorFlowTheme {
        InventoryItemDialog(
            modifier = Modifier
                .width(width = 300.dp),
            onInventoryEvent = { },
            inventoryState = InventoryState(
                tagList = listOf(tag1, tag2, tag3)
            )
        )
    }
}