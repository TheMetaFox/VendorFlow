package com.example.vendorflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.ViewModelProvider
import com.example.vendorflow.ApiModule.provideApi
import com.example.vendorflow.ApiModule.provideApiRepository
import com.example.vendorflow.ApiModule.provideClient
import com.example.vendorflow.DatabaseModule.provideDao
import com.example.vendorflow.DatabaseModule.provideDatabase
import com.example.vendorflow.DatabaseModule.provideDatabaseRepository
import com.example.vendorflow.data.VendorRepository
import com.example.vendorflow.data.notion.NotionRepository
import com.example.vendorflow.ui.NavGraph
import com.example.vendorflow.ui.screens.catalog.CatalogEvent
import com.example.vendorflow.ui.screens.catalog.CatalogState
import com.example.vendorflow.ui.screens.catalog.CatalogViewModel
import com.example.vendorflow.ui.screens.collections.CollectionsEvent
import com.example.vendorflow.ui.screens.collections.CollectionsState
import com.example.vendorflow.ui.screens.collections.CollectionsViewModel
import com.example.vendorflow.ui.screens.inventory.InventoryEvent
import com.example.vendorflow.ui.screens.inventory.InventoryState
import com.example.vendorflow.ui.screens.inventory.InventoryViewModel
import com.example.vendorflow.ui.screens.login.LoginEvent
import com.example.vendorflow.ui.screens.login.LoginState
import com.example.vendorflow.ui.screens.login.LoginViewModel
import com.example.vendorflow.ui.screens.sales.SalesEvent
import com.example.vendorflow.ui.screens.sales.SalesState
import com.example.vendorflow.ui.screens.sales.SalesViewModel
import com.example.vendorflow.ui.screens.transation.TransactionEvent
import com.example.vendorflow.ui.screens.transation.TransactionState
import com.example.vendorflow.ui.screens.transation.TransactionViewModel
import com.example.vendorflow.ui.theme.VendorFlowTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val vendorRepository: VendorRepository = provideDatabaseRepository(provideDao(provideDatabase(applicationContext)))
        val notionRepository: NotionRepository = provideApiRepository(provideApi(provideClient()))

        val viewModelFactory = ViewModelFactory(vendorRepository = vendorRepository, notionRepository = notionRepository)

        val loginViewModel = ViewModelProvider(
            owner = this,
            factory = viewModelFactory
        )[LoginViewModel::class.java]
        val transactionViewModel = ViewModelProvider(
            owner = this,
            factory = viewModelFactory
        )[TransactionViewModel::class.java]
        val inventoryViewModel = ViewModelProvider(
            owner = this,
            factory = viewModelFactory
        )[InventoryViewModel::class.java]
        val catalogViewModel = ViewModelProvider(
            owner = this,
            factory = viewModelFactory
        )[CatalogViewModel::class.java]
        val collectionsViewModel = ViewModelProvider(
            owner = this,
            factory = viewModelFactory
        )[CollectionsViewModel::class.java]
        val salesViewModel = ViewModelProvider(
            owner = this,
            factory = viewModelFactory
        )[SalesViewModel::class.java]

        val onLoginEvent: (LoginEvent) -> Unit = loginViewModel::onEvent
        val onTransactionEvent: (TransactionEvent) -> Unit = transactionViewModel::onEvent
        val onInventoryEvent: (InventoryEvent) -> Unit = inventoryViewModel::onEvent
        val onCatalogEvent: (CatalogEvent) -> Unit = catalogViewModel::onEvent
        val onCollectionsEvent: (CollectionsEvent) -> Unit = collectionsViewModel::onEvent
        val onSalesEvent: (SalesEvent) -> Unit = salesViewModel::onEvent

        setContent {
            VendorFlowTheme {
                val loginState: LoginState by loginViewModel.state.collectAsState()
                val transactionState: TransactionState by transactionViewModel.state.collectAsState()
                val inventoryState: InventoryState by inventoryViewModel.state.collectAsState()
                val catalogState: CatalogState by catalogViewModel.state.collectAsState()
                val collectionsState: CollectionsState by collectionsViewModel.state.collectAsState()
                val salesState: SalesState by salesViewModel.state.collectAsState()

                val snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
                val coroutineScope: CoroutineScope = rememberCoroutineScope()

                LaunchedEffect("") {
                    coroutineScope.launch {
//                        Log.i("MainActivity.kt", NotionApi.getUsers().toString().prettyPrint())
//                        Log.i("MainActivity.kt", NotionApi.getDatabaseTest().prettyPrint())
//                        Log.i("MainActivity.kt", NotionApi.getDatabaseQueryTest().prettyPrint())

//                        Log.i("MainActivity.kt", NotionApi.getProductCatalogDatabase().prettyPrint())
//                        Log.i("MainActivity.kt", NotionApi.queryProductCatalogDatabase().prettyPrint())
                    }
                    return@LaunchedEffect
                }

                NavGraph(
                    snackbarHostState = snackbarHostState,
                    coroutineScope = coroutineScope,
                    onLoginEvent = onLoginEvent,
                    onTransactionEvent = onTransactionEvent,
                    onInventoryEvent = onInventoryEvent,
                    onCatalogEvent = onCatalogEvent,
                    onCollectionsEvent = onCollectionsEvent,
                    onSalesEvent = onSalesEvent,
                    loginState = loginState,
                    transactionState = transactionState,
                    inventoryState = inventoryState,
                    catalogState = catalogState,
                    collectionsState = collectionsState,
                    salesState = salesState,
                )
            }
        }
    }
}