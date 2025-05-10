package com.example.vendorflow.ui.screens.catalog

import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.vendorflow.data.SortType
import com.example.vendorflow.data.room.entities.Product
import com.example.vendorflow.ui.theme.VendorFlowTheme
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
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
                .width(width = 300.dp),
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
                            text = { Text("Update Vendor Flow") },
                            onClick = {
                                onCatalogEvent(CatalogEvent.UpdateVendorFlow)
                                showDropDownMenu = false
                            },
                            leadingIcon = { Icon(Icons.Outlined.Info, "info") }
                        )
                        DropdownMenuItem(
                            text = { Text("Update Notion") },
                            onClick = {
                                onCatalogEvent(CatalogEvent.UpdateNotion)
                                showDropDownMenu = false
                            },
                            leadingIcon = { Icon(Icons.Outlined.Feedback, "feedback") }
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
                    onCatalogEvent(CatalogEvent.ShowCatalogItemDialog(item = null))
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
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(
                    Modifier
                        .width(width = 50.dp)
                )
                Text(
                    text = "ID",
                    modifier = Modifier
                        .fillMaxWidth(0.1f),
                    fontSize = 20.sp
                )
                Text(
                    text = "Name",
                    modifier = Modifier
                        .fillMaxWidth(0.6f),
                    fontSize = 20.sp
                )
                Text(
                    text = "Price",
                    modifier = Modifier
                        .fillMaxWidth(0.5f),
                    fontSize = 20.sp
                )
                Text(
                    text = "Cost",
                    modifier = Modifier
                        .fillMaxWidth(),
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
                    var isDropDownMenuVisible: Boolean by remember { mutableStateOf(false) }
                    var longPressOffset: DpOffset by remember { mutableStateOf(DpOffset.Zero) }
                    val density: Density = LocalDensity.current
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onCatalogEvent(CatalogEvent.ShowCatalogItemDialog(item = catalogItem))
                            }
                            .combinedClickable(
                                onClick = {
                                    onCatalogEvent(CatalogEvent.ShowCatalogItemDialog(item = catalogItem))
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
                                .fillMaxWidth(0.1f)
                        ) {
                            Text(
                                text = String.format(Locale.ENGLISH, "%03d", catalogItem.productId),
                                fontSize = 15.sp
                            )
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.6f)
                        ) {
                            Text(
                                text = catalogItem.productName,
                                fontSize = 15.sp
                            )
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.5f),
                        ) {
                            Text(
                                text = String.format(Locale.ENGLISH, "\$%.2f", catalogItem.price),
                                fontSize = 15.sp
                            )
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(),
                        ) {
                            Text(
                                text = String.format(Locale.ENGLISH, "\$%.2f", catalogItem.cost),
                                fontSize = 15.sp
                            )
                        }
                        DropdownMenu(
                            expanded = isDropDownMenuVisible,
                            onDismissRequest = { isDropDownMenuVisible = false },
                            offset = longPressOffset
                        ) {
                            DropdownMenuItem(
                                text = { Text(text = "Remove") },
                                onClick = {
                                    onCatalogEvent(CatalogEvent.DeleteCatalogItem(item = catalogItem))
                                }
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
            productId = 1,
            productName = "Hello Kitty Kandi",
//            collectionName = "Sanrio Collection",
            collectionId = 0,
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