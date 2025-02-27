package com.example.vendorflow.ui.screens.catalog

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.vendorflow.data.SortType
import com.example.vendorflow.data.entities.Product
import com.example.vendorflow.ui.theme.VendorFlowTheme
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    onCatalogEvent: (CatalogEvent) -> Unit,
    catalogState: CatalogState,
    ) {
    if (catalogState.isShowingCatalogItemDialog) {
        CatalogItemDialog(
            modifier = Modifier
                .size(width = 300.dp, height = 600.dp),
            onCatalogEvent = onCatalogEvent,
            catalogState = catalogState
        )
    }
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = "Catalog") },
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
            FloatingActionButton(
                onClick = {
                    onCatalogEvent(CatalogEvent.ShowCatalogItemDialog)
                },
                modifier = Modifier
            ) {
                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = "Add Catalog Item"
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(paddingValues = innerPadding)
                .padding(all = 10.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .width(315.dp)
                    .align(Alignment.End),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "ID",
                    modifier = Modifier
                        .width(60.dp),
                    fontSize = 20.sp
                )
                Text(
                    text = "Name",
                    modifier = Modifier
                        .width(135.dp),
                    fontSize = 20.sp
                )
                Text(
                    text = "Price",
                    modifier = Modifier
                        .width(50.dp),
                    fontSize = 20.sp
                )
                Text(
                    text = "Cost",
                    modifier = Modifier
                        .width(50.dp),
                    fontSize = 20.sp
                )
            }
            LazyColumn (
                modifier = Modifier
            ) {
                items(
                    count = catalogState.catalogList.size
                ) {
                    val catalogItem: Product = catalogState.catalogList[it]
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box {
                            Image(
                                imageVector = Icons.Default.ShoppingBag,
                                contentDescription = "product image",
                                modifier = Modifier
                                    .size(50.dp)
                            )
                            Image(
                                painter = rememberAsyncImagePainter(
                                    model = catalogItem.image,
                                    contentScale = ContentScale.Crop
                                ),
                                contentDescription = "product image",
                                modifier = Modifier
                                    .size(size = 50.dp),
                                contentScale = ContentScale.Crop
                            )
                        }
                        Box(
                            modifier = Modifier
                                .width(65.dp)
                        ) {
                            Text(
                                text = catalogItem.productId.toString().slice(0..1) + "-" + catalogItem.productId.toString().slice(2..5),
                                fontSize = 15.sp
                            )
                        }
                        Box(
                            modifier = Modifier
                                .width(135.dp)
                        ) {
                            Text(
                                text = catalogItem.productName,
                                fontSize = 15.sp
                            )
                        }
                        Box(
                            modifier = Modifier
                                .width(50.dp),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            Text(
                                text = String.format(Locale.ENGLISH, "\$%.2f", catalogItem.price),
                                fontSize = 15.sp
                            )
                        }
                        Box(
                            modifier = Modifier
                                .width(50.dp),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            Text(
                                text = String.format(Locale.ENGLISH, "\$%.2f", catalogItem.cost),
                                fontSize = 15.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CatalogScreenPreview() {
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
        CatalogScreen(
            navController = rememberNavController(),
            onCatalogEvent = {  },
            catalogState = CatalogState(
                catalogList = listOf(
                    product
                ),
                sortType = SortType.NAME
            )
        )
    }
}