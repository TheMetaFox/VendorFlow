package com.example.vendorflow.ui

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.vendorflow.ui.screens.catalog.CatalogEvent
import com.example.vendorflow.ui.screens.catalog.CatalogScreen
import com.example.vendorflow.ui.screens.catalog.CatalogState
import com.example.vendorflow.ui.screens.HomeScreen
import com.example.vendorflow.ui.screens.inventory.InventoryEvent
import com.example.vendorflow.ui.screens.inventory.InventoryScreen
import com.example.vendorflow.ui.screens.inventory.InventoryState
import com.example.vendorflow.ui.screens.login.LoginEvent
import com.example.vendorflow.ui.screens.login.LoginScreen
import com.example.vendorflow.ui.screens.login.LoginState
import com.example.vendorflow.ui.screens.transation.TransactionScreen
import com.example.vendorflow.ui.screens.transation.review.ReviewScreen
import com.example.vendorflow.ui.screens.sales.SalesEvent
import com.example.vendorflow.ui.screens.sales.SalesScreen
import com.example.vendorflow.ui.screens.sales.SalesState
import com.example.vendorflow.ui.screens.transation.TransactionEvent
import com.example.vendorflow.ui.screens.transation.TransactionState
import kotlinx.coroutines.CoroutineScope

@Composable
fun NavGraph(
    snackbarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope,
    onLoginEvent: (LoginEvent) -> Unit,
    onTransactionEvent: (TransactionEvent) -> Unit,
    onInventoryEvent: (InventoryEvent) -> Unit,
    onCatalogEvent: (CatalogEvent) -> Unit,
    onSalesEvent: (SalesEvent) -> Unit,
    loginState: LoginState,
    transactionState: TransactionState,
    inventoryState: InventoryState,
    catalogState: CatalogState,
    salesState: SalesState,
) {
    val navController: NavHostController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(route = Screen.Login.route) {
            LoginScreen(
                navController = navController,
                onLoginEvent = onLoginEvent,
                loginState = loginState
            )
        }
        composable(route = Screen.Home.route) {
            HomeScreen(
                navController = navController,
            )
        }
        composable(route = Screen.Transaction.route) {
            TransactionScreen(
                navController = navController,
                snackbarHostState = snackbarHostState,
                coroutineScope = coroutineScope,
                onTransactionEvent = onTransactionEvent,
                transactionState = transactionState,
            )
        }
        composable(route = Screen.Inventory.route) {
            InventoryScreen(
                navController = navController,
                onInventoryEvent = onInventoryEvent,
                inventoryState = inventoryState
            )
        }
        composable(route = Screen.Catalog.route) {
            CatalogScreen(
                navController = navController,
                onCatalogEvent = onCatalogEvent,
                catalogState = catalogState
            )
        }
        composable(route = Screen.Sales.route) {
            SalesScreen(
                navController = navController,
                onSalesEvent = onSalesEvent,
                salesState = salesState
            )
        }
        composable(route = Screen.Review.route) {
            ReviewScreen(
                navController = navController,
                snackbarHostState = snackbarHostState,
                coroutineScope = coroutineScope,
                onTransactionEvent = onTransactionEvent,
                transactionState = transactionState
            )
        }
    }
}
