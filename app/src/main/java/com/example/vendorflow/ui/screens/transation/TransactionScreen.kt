package com.example.vendorflow.ui.screens.transation

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.vendorflow.data.room.entities.Product
import com.example.vendorflow.data.room.entities.Tag
import com.example.vendorflow.ui.Screen
import com.example.vendorflow.ui.screens.inventory.InventoryEvent
import com.example.vendorflow.ui.theme.VendorFlowTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.Locale

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
                    onTransactionEvent(TransactionEvent.DisplayItemizedTransaction(itemList = transactionState.itemQuantityList))
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
//        val selected: MutableList<String> by remember { mutableStateOf(mutableListOf()) }
        Column(
            modifier = Modifier
                .padding(paddingValues = innerPadding)
                .verticalScroll(state = rememberScrollState()),
        ) {
            LazyHorizontalStaggeredGrid(
                rows = StaggeredGridCells.FixedSize(size = 36.dp),
                modifier = Modifier
                    .height(height = 100.dp)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(all = 5.dp),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalItemSpacing = 5.dp
            ) {
                items(items = transactionState.tagList) { tag ->
                    FilterChip(
                        selected = (transactionState.selectedTags.contains(element = tag)),
                        onClick = {
                            onTransactionEvent(TransactionEvent.UpdateSelectedTags(tag = tag))
                        },
                        label = {
                            Text(text = tag.tagName)
                        }
                    )
                }
            }
            TextField(
                value = transactionState.searchText,
                onValueChange = { onTransactionEvent(TransactionEvent.UpdateSearchField(text = it))},
                modifier = Modifier
                    .fillMaxWidth()
            )
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 100.dp),
                modifier = Modifier
                    .heightIn(0.dp, 2000.dp)
                    .fillMaxSize()
            )  {
                items(count = transactionState.productList.size) { it ->
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
//                                    .size(width = 150.dp, height = 200.dp)
                            .wrapContentHeight()
                            .padding(all = 5.dp),
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
                                        .aspectRatio(ratio = 1f)
                                        .fillMaxWidth()
                                )
                                Image(
                                    painter = rememberAsyncImagePainter(
                                        model = product.image,
                                        contentScale = ContentScale.Crop
                                    ),
                                    contentDescription = "product image",
                                    modifier = Modifier
                                        .aspectRatio(ratio = 1f)
                                        .fillMaxWidth(),
                                    contentScale = ContentScale.Crop
                                )
                            }
                            Column(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .padding(all = 5.dp),
                                verticalArrangement = Arrangement.SpaceBetween,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = product.productName,
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    fontSize = 14.sp,
                                    minLines = 2
                                )
                                Text(
                                    text = String.format(Locale.ENGLISH, "$%.2f", product.price),
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    color = MaterialTheme.colorScheme.primary,
                                    fontSize = 12.sp,
                                    textAlign = TextAlign.End,
                                    lineHeight = 10.sp,
                                    minLines = 1
                                )
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(space = 5.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Button(
                                        onClick = {
                                            onTransactionEvent(
                                                TransactionEvent.DecreaseItemQuantity(
                                                    product = product
                                                )
                                            )
                                        },
                                        modifier = Modifier
                                            .size(size = 20.dp),
                                        enabled = (transactionState.itemQuantityList.containsKey(
                                            product
                                        )),
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
                                            onTransactionEvent(
                                                TransactionEvent.IncreaseItemQuantity(
                                                    product = product
                                                )
                                            )
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
                item {
                    Spacer(
                        modifier = Modifier
                            .height(height = 80.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TransactionScreenPreview() {
    VendorFlowTheme {
        val product1 = Product(
            productId = 1,
            productName = "Hello Kitty Bracelet",
            image = Uri.EMPTY,
            price = 2f,
            cost = 0.23f,
            stock = 10,
        )
        val product2 = Product(
            productId = 2,
            productName = "Libra Bracelet",
            image = Uri.EMPTY,
            price = 2f,
            cost = 0.23f,
            stock = 10,
        )
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

        TransactionScreen(
            navController = rememberNavController(),
            snackbarHostState = SnackbarHostState(),
            coroutineScope = rememberCoroutineScope(),
            onTransactionEvent = { },
            transactionState = TransactionState(
                productList = arrayListOf(product1, product2),
                itemQuantityList = mapOf(
                    product1 to 2
                ),
                tagList = listOf(tag1, tag2, tag3)
            ),
        )
    }
}