package com.example.vendorflow.ui

sealed class Screen(val route: String) {
    data object Login: Screen(route = "login_screen")
    data object Home: Screen(route = "home_screen")
    data object Transaction: Screen(route = "transaction_screen")
    data object Inventory: Screen(route = "inventory_screen")
    data object Catalog: Screen(route = "catalog_screen")
    data object Collections: Screen(route = "collections_screen")
    data object Sales: Screen(route = "sales_screen")
    data object Review: Screen(route = "review_screen")
}