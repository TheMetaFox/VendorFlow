package com.example.vendorflow.ui.screens.inventory

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.vendorflow.data.entities.Product
import com.example.vendorflow.ui.theme.VendorFlowTheme
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    onInventoryEvent: (InventoryEvent) -> Unit,
    inventoryState: InventoryState,
    ) {
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
                colors = TopAppBarDefaults.topAppBarColors().copy(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },

        ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(paddingValues = innerPadding)
                .padding(all = 10.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Row(
                modifier = Modifier
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
                    Text(text = String.format(Locale.ENGLISH, "\$%.2f", inventoryState.totalInventoryPrice), fontSize = 18.sp)
                }
                Column(
                    modifier = Modifier,
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Cost", fontSize = 12.sp)
                    Text(text = String.format(Locale.ENGLISH, "\$%.2f", inventoryState.totalInventoryCost), fontSize = 18.sp)
                }
                Column(
                    modifier = Modifier,
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Profit", fontSize = 12.sp)
                    Text(text = String.format(Locale.ENGLISH, "\$%.2f", (inventoryState.totalInventoryPrice - inventoryState.totalInventoryCost)), fontSize = 18.sp)
                }
            }
            LazyColumn {
                items(inventoryState.inventoryList.size) {
                    val inventoryItem: Product = inventoryState.inventoryList[it]

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box {
                                Image(
                                    imageVector = Icons.Default.ShoppingBag,
                                    contentDescription = "product image",
                                    modifier = Modifier.size(80.dp),
                                )
                                Image(
                                    painter = rememberAsyncImagePainter(
                                        model = inventoryItem.image,
                                        contentScale = ContentScale.Crop
                                    ),
                                    contentDescription = "product image",
                                    modifier = Modifier.size(80.dp),
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
                                        text = inventoryItem.productId.toString().slice(0..1) + "-" + inventoryItem.productId.toString().slice(2..5),
                                        fontSize = 10.sp
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
                                        text = String.format(Locale.ENGLISH, "\$%.2f", inventoryItem.price),
                                        fontSize = 12.sp
                                    )
                                    Text(
                                        text = String.format(Locale.ENGLISH, "\$%.2f", inventoryItem.cost),
                                        fontSize = 12.sp
                                    )
                                    Text(
                                        text = String.format(Locale.ENGLISH, "\$%.2f", (inventoryItem.price - inventoryItem.cost)),
                                        fontSize = 12.sp
                                    )
                                }
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
            productId = 246368,
            productName = "Hello Kitty Kandi",
            collectionName = "Sanrio Collection",
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