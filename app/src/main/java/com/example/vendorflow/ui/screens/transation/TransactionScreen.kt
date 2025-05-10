package com.example.vendorflow.ui.screens.transation

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.vendorflow.data.room.entities.Collection
import com.example.vendorflow.data.room.entities.Product
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
        var selected: String by remember { mutableStateOf(value = "") }
        Column(
            modifier = Modifier
                .padding(paddingValues = innerPadding)
                .padding(all = 10.dp)
                .padding(bottom = 75.dp)
                .height(800.dp)
                .verticalScroll(state = rememberScrollState()),
        ) {
            transactionState.collectionList.forEach { collection ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selected = if (selected == collection.collectionName) "" else collection.collectionName},
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = collection.collectionName,
                        fontSize = 24.sp
                    )
                    if (selected == collection.collectionName) {
                        Icon(
                            imageVector = Icons.Default.ArrowDropUp,
                            contentDescription = null,
                            modifier = Modifier
                                .size(size = 40.dp)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = null,
                            modifier = Modifier
                                .size(size = 40.dp)
                        )
                    }
                }
                if (selected == collection.collectionName) {
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 128.dp),
                        modifier = Modifier
                            .wrapContentHeight()
                            .heightIn(0.dp, 2000.dp)
                    ) {
                        items(transactionState.productList.filter { it.collectionId == collection.collectionId }.size) { it ->
                            val product: Product = transactionState.productList.filter { it.collectionId == collection.collectionId }[it]
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
//                                    .size(width = 150.dp, height = 200.dp)
                                    .wrapContentHeight()
                                    .padding(5.dp),
                                enabled = ((transactionState.itemQuantityList[product] != product.stock) && (product.stock != 0))
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize(),
                                    verticalArrangement = Arrangement.SpaceBetween,
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
                                    Column(
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .padding(5.dp),
                                        verticalArrangement = Arrangement.SpaceBetween,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = product.productName,
                                            modifier = Modifier
                                                .fillMaxWidth(),
                                            fontSize = 14.sp
                                        )
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
                                                text = if (transactionState.itemQuantityList[product] == null) "0" else transactionState.itemQuantityList[product].toString(),
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
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TransactionScreenPreview() {
    VendorFlowTheme {
        val collection1 = Collection(
            collectionId = 1,
            collectionName = "Sanrio"
        )
        val product1 = Product(
            productId = 1,
            productName = "Hello Kitty Bracelet",
            collectionId = 1,
            image = Uri.EMPTY,
            price = 2f,
            cost = 0.23f,
            stock = 10,
        )
        val collection2 = Collection(
            collectionId = 2,
            collectionName = "Horoscope"
        )
        val product2 = Product(
            productId = 2,
            productName = "Libra Bracelet",
            collectionId = 2,
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
                collectionList = arrayListOf(collection1, collection2),
                productList = arrayListOf(product1, product2),
                itemQuantityList = mapOf(
                    product1 to 2
                )
            ),
        )
    }
}