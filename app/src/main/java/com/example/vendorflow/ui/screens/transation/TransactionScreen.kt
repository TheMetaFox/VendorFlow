package com.example.vendorflow.ui.screens.transation

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
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
import com.example.vendorflow.ui.Screen
import com.example.vendorflow.ui.theme.VendorFlowTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope,
    onTransactionEvent: (TransactionEvent) -> Unit,
    transactionState: TransactionState,
    ) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = "Transaction") },
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
        floatingActionButton = {
            Button(
                onClick = {
                    onTransactionEvent(TransactionEvent.DisplayItemizedTransaction(transactionState.itemQuantityList))
                    navController.navigate(route = Screen.Review.route)
                },
                modifier = Modifier
                    .size(width = 350.dp, height = 75.dp)
            ) {
                Text(
                    text = "Review Transaction (${transactionState.itemQuantityList.size})",
                    fontSize = 28.sp
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(paddingValues = innerPadding)
                .padding(all = 10.dp)
                .fillMaxSize(),
        ) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 128.dp),
            ) {
                items(transactionState.productList.size) {
                    val product: Product = transactionState.productList[it]

                    Card(
                        onClick = {
                            onTransactionEvent(TransactionEvent.IncreaseItemQuantity(product = product))
                            if (transactionState.itemQuantityList[product] == product.stock) {
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar(
                                        message = "Out Of Stock"
                                    )
                                }
                            }
                        },
                        modifier = Modifier
                            .size(width = 150.dp, height = 250.dp)
                            .padding(5.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(10.dp),
                            verticalArrangement = Arrangement.SpaceBetween,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box {
                                    Image(
                                        imageVector = Icons.Default.ShoppingBag,
                                        contentDescription = "product image",
                                        modifier = Modifier
                                            .size(128.dp)
                                    )
                                    Image(
                                        painter = rememberAsyncImagePainter(
                                            model = product.image,
                                            contentScale = ContentScale.Crop
                                        ),
                                        contentDescription = "product image",
                                        modifier = Modifier.size(128.dp),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                                Text(
                                    text = product.productName,
                                    fontSize = 14.sp
                                )
                            }
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Button(
                                    onClick = {
                                        onTransactionEvent(TransactionEvent.DecreaseItemQuantity(product = product))
                                    },
                                    modifier = Modifier
                                        .size(size = 20.dp),
                                    enabled = (transactionState.itemQuantityList.containsKey(product)),
                                    contentPadding = PaddingValues(all = 0.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Remove,
                                        contentDescription = "Decrease Quantity",
                                        modifier = Modifier
                                            .size(size = 15.dp)
                                    )
                                }
                                Text(
                                    text = transactionState.itemQuantityList[product].toString(),
                                    fontSize = 16.sp
                                )
                                Button(
                                    onClick = {
                                        onTransactionEvent(TransactionEvent.IncreaseItemQuantity(product = product))
                                        if (transactionState.itemQuantityList[product] == product.stock) {
                                            coroutineScope.launch {
                                                snackbarHostState.showSnackbar(
                                                    message = "Out Of Stock"
                                                )
                                            }
                                        }
                                    },
                                    modifier = Modifier
                                        .size(size = 20.dp),
                                    enabled = ((transactionState.itemQuantityList[product] != product.stock) && (product.stock != 0)),
                                    contentPadding = PaddingValues(all = 0.dp)
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
}

@Preview(showBackground = true)
@Composable
fun TransactionScreenPreview() {
    VendorFlowTheme {
        val product = Product(
            productId = 2463681,
            productName = "Hello Kitty Kandi",
            collectionName = "Sanrio Collection",
            image = Uri.EMPTY,
            price = 2f,
            cost = 0.23f,
            stock = 10,
        )

        TransactionScreen(
            navController = rememberNavController(),
            snackbarHostState = SnackbarHostState(),
            coroutineScope = rememberCoroutineScope(),
            onTransactionEvent = { },
            transactionState = TransactionState(
                productList = arrayListOf(product),
                itemQuantityList = mapOf(
                    product to 2
                )
            ),
        )
    }
}