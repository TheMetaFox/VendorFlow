package com.example.vendorflow.ui.screens.collections

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Add
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.vendorflow.data.room.entities.Collection
import com.example.vendorflow.ui.theme.VendorFlowTheme
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun CollectionsScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    onCollectionsEvent: (CollectionsEvent) -> Unit,
    collectionsState: CollectionsState,
) {
    if (collectionsState.isShowingCollectionDialog) {
        CollectionDialog(
            modifier = Modifier
                .width(width = 300.dp),
            onCollectionsEvent = onCollectionsEvent,
            collectionsState = collectionsState
        )
    }
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = "Collections") },
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
                    onCollectionsEvent(CollectionsEvent.ShowCollectionDialog(collection = null))
                },
                modifier = Modifier
            ) {
                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = "Add Collection"
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
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.3f)
                ) {
                    Text(
                        text = "ID",
                        fontSize = 36.sp
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Name",
                        fontSize = 36.sp
                    )
                }
            }
            LazyColumn (
                modifier = Modifier
            ) {
                items(
                    count = collectionsState.collectionList.size
                ) {
                    val collection: Collection = collectionsState.collectionList[it]
                    var isDropDownMenuVisible: Boolean by remember { mutableStateOf(false) }
                    var longPressOffset: DpOffset by remember { mutableStateOf(DpOffset.Zero) }
                    val density: Density = LocalDensity.current
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 5.dp)
                            .pointerInput(true) {
                                detectTapGestures(
                                    onLongPress = { longPressOffset = with (density) { DpOffset(it.x.toDp(), it.y.toDp()) }}
                                )
                            }
                            .combinedClickable(
                                onClick = {
                                    onCollectionsEvent(CollectionsEvent.ShowCollectionDialog(collection = collection))
                                },
                                onLongClick = {
                                    isDropDownMenuVisible = true
                                }
                            ),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.3f)
                        ) {
                            Text(
                                text = String.format(Locale.ENGLISH, "%03d", collection.collectionId),
                                fontSize = 26.sp
                            )
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = collection.collectionName,
                                fontSize = 26.sp
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
                                    onCollectionsEvent(CollectionsEvent.DeleteCollection(collection = collection))
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


        val collection = Collection(
            collectionId = 1,
            collectionName = "Sanrio"
        )
        CollectionsScreen(
            navController = rememberNavController(),
            onCollectionsEvent = {  },
            collectionsState = CollectionsState(
                collectionList = listOf(
                    collection
                )
            )
        )
    }
}