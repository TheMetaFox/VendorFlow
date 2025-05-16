package com.example.vendorflow.ui.screens.transation.review

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.material3.RadioButton
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.vendorflow.data.PaymentMethod
import com.example.vendorflow.data.room.entities.Product
import com.example.vendorflow.ui.screens.transation.TransactionEvent
import com.example.vendorflow.ui.screens.transation.TransactionState
import com.example.vendorflow.ui.theme.VendorFlowTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewScreen(
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
                title = { Text(text = "Review") },
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
        onTransactionEvent(TransactionEvent.CalculateTotal)
        Column(
            modifier = Modifier
                .padding(paddingValues = innerPadding)
                .padding(all = 10.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column {
                transactionState.itemQuantityList.forEach { (product, quantity) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box {
                                Image(
                                    imageVector = Icons.Default.ShoppingBag,
                                    contentDescription = "product image",
                                    modifier = Modifier.size(100.dp)
                                )
                                Image(
                                    painter = rememberAsyncImagePainter(
                                        model = product.image,
                                        contentScale = ContentScale.Crop
                                    ),
                                    contentDescription = "product image",
                                    modifier = Modifier.size(100.dp),
                                    contentScale = ContentScale.Crop
                                )
                            }
                            Column(
                                verticalArrangement = Arrangement.spacedBy(10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Column {
                                    Text(
                                        text = product.productName,
                                        fontSize = 20.sp
                                    )
                                    Text(
                                        text = String.format(Locale.ENGLISH, "\$%.2f", product.price),
                                        fontSize = 18.sp
                                    )
                                }
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Button(
                                        onClick = {
                                            onTransactionEvent(TransactionEvent.DecreaseItemQuantity(product = product))
                                            onTransactionEvent(TransactionEvent.CalculateTotal)
                                        },
                                        modifier = Modifier
                                            .size(size = 30.dp),
                                        enabled = (transactionState.itemQuantityList.containsKey(product)),
                                        contentPadding = PaddingValues(all = 0.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Outlined.Remove,
                                            contentDescription = "Decrease Quantity",
                                            modifier = Modifier
                                                .size(size = 25.dp)
                                        )
                                    }
                                    Text(
                                        text = quantity.toString(),
                                        fontSize = 16.sp
                                    )
                                    Button(
                                        onClick = {
                                            onTransactionEvent(TransactionEvent.IncreaseItemQuantity(product = product))
                                            onTransactionEvent(TransactionEvent.CalculateTotal)
                                            if (transactionState.itemQuantityList[product] == product.stock) {
                                                coroutineScope.launch {
                                                    snackbarHostState.showSnackbar(
                                                        message = "Out Of Stock"
                                                    )
                                                }
                                            }
                                        },
                                        modifier = Modifier
                                            .size(size = 30.dp),
                                        enabled = ((transactionState.itemQuantityList[product] != product.stock) && (product.stock != 0)),
                                        contentPadding = PaddingValues(all = 0.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Outlined.Add,
                                            contentDescription = "Increase Quantity",
                                            modifier = Modifier
                                                .size(size = 25.dp)
                                        )
                                    }
                                }
                            }
                        }
                        Text(
                            text = String.format(Locale.ENGLISH, "\$%.2f", (product.price*quantity)),
                            fontSize = 18.sp
                        )
                    }
                    
                }
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Total",
                        fontSize = 36.sp
                    )
                    Text(
                        text = String.format(Locale.ENGLISH, "\$%.2f", transactionState.totalAmount),
                        fontSize = 28.sp
                    )
                }
                val scrollState = rememberScrollState()
                Row(
                    modifier = Modifier
                        .height(height = 60.dp)
                        .horizontalScroll(
                            state = scrollState,
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    PaymentMethod.entries.forEach { paymentMethod ->
                        Row(
                            modifier = Modifier
                                .clickable {
                                    onTransactionEvent(TransactionEvent.SetPaymentMethod(paymentMethod))
                                },
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            RadioButton(
                                selected = paymentMethod == transactionState.paymentMethod,
                                onClick = { onTransactionEvent(TransactionEvent.SetPaymentMethod(paymentMethod)) }
                            )
                            Text(
                                text = paymentMethod.name
                            )
                        }
                    }
                }
                Button(
                    onClick = {
                        onTransactionEvent(TransactionEvent.ProcessTransaction)
                        navController.navigateUp()
                    },
                    modifier = Modifier
                        .size(width = 300.dp, height = 65.dp)
                ) {
                    Text(
                        text = "Confirm",
                        fontSize = 26.sp
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ReviewScreenPreview() {
    VendorFlowTheme {
        val product = Product(
            productName = "Hello Kitty Kandi",
            image = Uri.EMPTY,
            price = 2f,
            cost = 0.23f,
            stock = 10,
        )

        ReviewScreen(
            navController = rememberNavController(),
            snackbarHostState = SnackbarHostState(),
            coroutineScope = rememberCoroutineScope(),
            onTransactionEvent = { },
            transactionState = TransactionState(
                itemQuantityList = mapOf(
                    Pair(product, 2)
                ),
                totalAmount = 4f,
                paymentMethod = PaymentMethod.CASH
            ),
        )
    }
}