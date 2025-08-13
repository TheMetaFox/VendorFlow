package com.example.vendorflow.ui.screens.sales

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.vendorflow.data.enums.PaymentMethod
import com.example.vendorflow.data.room.entities.Sale
import com.example.vendorflow.data.room.entities.relations.SoldItem
import com.example.vendorflow.ui.theme.VendorFlowTheme
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun SalesScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    onSalesEvent: (SalesEvent) -> Unit,
    salesState: SalesState,
    ) {
    onSalesEvent(SalesEvent.LoadSalesData)
    if (salesState.isShowingSaleDialog) {
        SaleDialog(
            modifier = Modifier
                .width(width = 300.dp),
            onSalesEvent = onSalesEvent,
            salesState = salesState
        )
    }
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = "Sales") },
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
//        floatingActionButton = {
//            Button(
//                onClick = {
//                },
//                modifier = Modifier
//                    .size(width = 350.dp, height = 75.dp),
//                colors = ButtonDefaults.buttonColors().copy(
//                    containerColor = Color(255, 100, 200)
//                )
//            ) {
//                Text(
//                    text = "Sort Skittles",
//                    fontSize = 28.sp
//                )
//            }
//        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(paddingValues = innerPadding)
                .padding(start = 10.dp, top = 10.dp, end = 10.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                Text(
                    text = "Date/Time",
                    modifier = Modifier
                        .fillMaxWidth(0.3f),
                    fontSize = 20.sp
                )
                Text(
                    text = "Description",
                    modifier = Modifier
                        .fillMaxWidth(0.7f),
                    fontSize = 20.sp
                )
                Text(
                    text = "Amount",
                    modifier = Modifier
                        .fillMaxWidth(),
                    fontSize = 20.sp,
                    textAlign = TextAlign.Right
                )
            }
            LazyColumn(
                modifier = Modifier
            ) {
                items(count = salesState.salesList.size) {
                    val sale: Sale = salesState.salesList[it]
                    val soldItems: List<SoldItem> = salesState.soldItemsList[it]
                    var isDropDownMenuVisible: Boolean by remember { mutableStateOf(false) }
                    var longPressOffset: DpOffset by remember { mutableStateOf(DpOffset.Zero) }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 7.dp)
//                            .clickable { onSalesEvent(SalesEvent.ShowSaleDialog(sale = sale, soldItems = soldItems)) }
                            .combinedClickable(
                                onClick = {
                                    onSalesEvent(SalesEvent.ShowSaleDialog(sale = sale, soldItems = soldItems))
                                },
                                onLongClick = {
                                    isDropDownMenuVisible = true
                                }
                            ),
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(0.3f),
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = sale.dateTime.toLocalDate().toString(),
                                fontSize = 14.sp
                            )
                            Text(
                                text = sale.dateTime.toLocalTime().truncatedTo(ChronoUnit.SECONDS).toString(),
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 10.sp,
                                lineHeight = 10.sp
                            )
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(0.75f)
                        ) {
                            soldItems.forEach { soldItem ->
                                Text(
                                    text = soldItem.quantity.toString()+" "+soldItem.productName,
                                    fontSize = 15.sp
                                )
                            }
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.End
                        ) {
                            Text(
                                text = String.format(Locale.ENGLISH, "$%.2f", sale.amount),
                                fontSize = 15.sp,
                            )
                            Text(
                                text = sale.paymentMethod.toString(),
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 10.sp,
                                lineHeight = 10.sp
                            )
                        }
                        DropdownMenu(
                            expanded = isDropDownMenuVisible,
                            onDismissRequest = { isDropDownMenuVisible = false },
                            offset = longPressOffset
                        ) {
                            DropdownMenuItem(
                                text = { Text(text = "Edit") },
                                onClick = {
                                    onSalesEvent(SalesEvent.ShowSaleDialog(sale = sale, soldItems = soldItems))
                                }
                            )
                        }
                    }
                }
//                item {
//                    Spacer(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .height(height = 75.dp)
//                    )
//                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SalesScreenPreview() {
    VendorFlowTheme {
        val sale = Sale(
            dateTime = LocalDateTime.of(2020, 2, 20, 22, 22, 22),
            amount = 6f,
            PaymentMethod.CASH
        )
        val soldItem1 = SoldItem(
            saleId = sale.saleId,
            productName = "Hello Kitty Kandi",
            quantity = 2
        )
        val soldItem2 = SoldItem(
            saleId = sale.saleId,
            productName = "Hello Kitty Kandi",
            quantity = 1
        )

        SalesScreen(
            navController = rememberNavController(),
            onSalesEvent = {  },
            salesState = SalesState(
                salesList = listOf(
                    sale
                ),
                soldItemsList = listOf(
                    listOf(
                        soldItem1,
                        soldItem2
                    )
                )
            )
        )
    }
}