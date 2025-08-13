package com.example.vendorflow.ui.screens.inventory

import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Feedback
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.vendorflow.data.room.entities.Product
import com.example.vendorflow.data.enums.SyncSource
import com.example.vendorflow.ui.theme.VendorFlowTheme
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun InventoryScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    onInventoryEvent: (InventoryEvent) -> Unit,
    inventoryState: InventoryState,
    ) {
    if (inventoryState.isShowingProductDialog) {
        InventoryItemDialog(
            modifier = Modifier
                .width(width = 300.dp),
            onInventoryEvent = onInventoryEvent,
            inventoryState = inventoryState
        )
    } else if (inventoryState.isShowingConfirmationDialog) {
        SyncConfirmationDialog(
            onInventoryEvent = onInventoryEvent,
            inventoryState = inventoryState
        )
    }
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = "Inventory") },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.navigateUp()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    var showDropDownMenu by remember { mutableStateOf(false) }
                    IconButton(
                        onClick = { showDropDownMenu = true }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.MoreVert,
                            contentDescription = "more vertical",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                    DropdownMenu(
                        expanded = showDropDownMenu,
                        onDismissRequest = { showDropDownMenu = false },
                    ) {
                        DropdownMenuItem(
                            text = { Text(text = "Update Vendor Flow") },
                            onClick = {
                                onInventoryEvent(InventoryEvent.ShowConfirmationDialog(source = SyncSource.NOTION))
                                showDropDownMenu = false
                            },
                            leadingIcon = { Icon(imageVector = Icons.Outlined.Info, contentDescription = "info") }
                        )
                        DropdownMenuItem(
                            text = { Text(text = "Update Notion") },
                            onClick = {
                                onInventoryEvent(InventoryEvent.ShowConfirmationDialog(source = SyncSource.VENDOR_FLOW))
                                showDropDownMenu = false
                            },
                            leadingIcon = { Icon(imageVector = Icons.Outlined.Feedback, "feedback") }
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors().copy(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onInventoryEvent(InventoryEvent.ShowProductDialog(product = null))
                },
                modifier = Modifier
            ) {
                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = "Add Product Item"
                )
            }
        }

    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(paddingValues = innerPadding)
                .padding(start = 10.dp, top = 10.dp, end = 10.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {
            Row(
                modifier = Modifier
                    .padding(bottom = 10.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier,
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Retail", fontSize = 12.sp)
                    Text(
                        text = String.format(Locale.ENGLISH, format = "$%.2f", inventoryState.totalInventoryPrice),
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 18.sp
                    )
                }
                Column(
                    modifier = Modifier,
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Cost", fontSize = 12.sp)
                    Text(
                        text = String.format(Locale.ENGLISH, "$%.2f", inventoryState.totalInventoryCost),
                        color = MaterialTheme.colorScheme.secondary,
                        fontSize = 18.sp
                    )
                }
                Column(
                    modifier = Modifier,
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Profit", fontSize = 12.sp)
                    Text(
                        text = String.format(Locale.ENGLISH, "$%.2f", (inventoryState.totalInventoryPrice - inventoryState.totalInventoryCost)),
                        color = MaterialTheme.colorScheme.tertiary,
                        fontSize = 18.sp
                    )
                }
            }
            TextField(
                value = inventoryState.searchText,
                onValueChange = { onInventoryEvent(InventoryEvent.UpdateSearchField(text = it))},
                modifier = Modifier
                    .fillMaxWidth()
            )
            LazyColumn {
                items(count = inventoryState.inventoryList.size) {
                    val inventoryItem: Product = inventoryState.inventoryList[it]
                    var isDropDownMenuVisible: Boolean by remember { mutableStateOf(false) }
                    var longPressOffset: DpOffset by remember { mutableStateOf(DpOffset.Zero) }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            modifier = Modifier
                                .combinedClickable(
                                    onClick = {
                                        onInventoryEvent(InventoryEvent.ShowProductDialog(product = inventoryItem))
                                    },
                                    onLongClick = {
                                        isDropDownMenuVisible = true
                                    }
                                ),
                            horizontalArrangement = Arrangement.spacedBy(5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box {
                                Image(
                                    imageVector = Icons.Default.ShoppingBag,
                                    contentDescription = "product image",
                                    modifier = Modifier.size(100.dp),
                                )
                                Image(
                                    painter = rememberAsyncImagePainter(
                                        model = inventoryItem.image,
                                        contentScale = ContentScale.Crop
                                    ),
                                    contentDescription = "product image",
                                    modifier = Modifier.size(100.dp),
                                    contentScale = ContentScale.Crop
                                )
                            }
                            Column(
                                verticalArrangement = Arrangement.spacedBy(20.dp),
                                horizontalAlignment = Alignment.Start
                            ) {
                                Column(
                                    verticalArrangement = Arrangement.Top,
                                    horizontalAlignment = Alignment.Start
                                ) {
                                    Text(
                                        text = String.format(Locale.ENGLISH, "%03d", inventoryItem.productId),
                                        color = MaterialTheme.colorScheme.primary,
                                        fontSize = 10.sp,
                                        fontStyle = FontStyle.Italic
                                    )
                                    Text(
                                        text = inventoryItem.productName,
                                        fontSize = 14.sp
                                    )
                                }
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = String.format(Locale.ENGLISH, "$%.2f", inventoryItem.price),
                                        color = MaterialTheme.colorScheme.primary,
                                        fontSize = 12.sp
                                    )
                                    Text(
                                        text = String.format(Locale.ENGLISH, "$%.2f", inventoryItem.cost),
                                        color = MaterialTheme.colorScheme.secondary,
                                        fontSize = 12.sp
                                    )
                                    Text(
                                        text = String.format(Locale.ENGLISH, "$%.2f", (inventoryItem.price - inventoryItem.cost)),
                                        color = MaterialTheme.colorScheme.tertiary,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                            DropdownMenu(
                                expanded = isDropDownMenuVisible,
                                onDismissRequest = { isDropDownMenuVisible = false },
                                offset = longPressOffset
                            ) {
                                DropdownMenuItem(
                                    text = { Text(text = "Edit") },
                                    onClick = {
                                        onInventoryEvent(InventoryEvent.ShowProductDialog(product = inventoryItem))
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text(text = "Remove") },
                                    onClick = {
                                        onInventoryEvent(InventoryEvent.DeleteProductItem(product = inventoryItem))
                                    }
                                )
                            }
                        }
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                onClick = {
                                    onInventoryEvent(InventoryEvent.DecreaseProductStock(product = inventoryItem))
                                },
                                modifier = Modifier
                                    .size(size = 20.dp),
                                enabled = (inventoryItem.stock != 0),
                                contentPadding = PaddingValues(all = 2.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Remove,
                                    contentDescription = "Decrease Quantity",
                                    modifier = Modifier
                                        .size(size = 15.dp)
                                )
                            }
                            Text(
                                text = inventoryItem.stock.toString()
                            )
                            Button(
                                onClick = {
                                    onInventoryEvent(InventoryEvent.IncreaseProductStock(product = inventoryItem))
                                },
                                modifier = Modifier
                                    .size(size = 20.dp),
                                contentPadding = PaddingValues(all = 2.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Add,
                                    contentDescription = "Increase Quantity",
                                    modifier = Modifier
                                        .size(size = 15.dp)

                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InventoryScreenPreview() {
    VendorFlowTheme {
        val product = Product(
            productId = 1,
            productName = "Hello Kitty Kandi",
            image = Uri.EMPTY,
            price = 2f,
            cost = 0.23f,
            stock = 10,
        )
        InventoryScreen(
            navController = rememberNavController(),
            onInventoryEvent = {  },
            inventoryState = InventoryState(
                inventoryList = listOf(
                    product
                ),
                totalInventoryPrice = 20f,
                totalInventoryCost = 2.3f
            )
        )
    }
}