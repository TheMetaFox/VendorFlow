package com.example.vendorflow.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.vendorflow.ui.Screen
import com.example.vendorflow.ui.theme.VendorFlowTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    ) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = "Home") },
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
        Column(
            modifier = Modifier
                .padding(paddingValues = innerPadding)
                .padding(all = 10.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    navController.navigate(route = Screen.Transaction.route)
                },
                modifier = Modifier
                    .size(300.dp, 75.dp)
            ) {
                Text(
                    text = "Log Transaction",
                    fontSize = 20.sp
                )
            }
            Button(
                onClick = {
                    navController.navigate(route = Screen.Inventory.route)
                },
                modifier = Modifier
                    .size(300.dp, 75.dp)
            ) {
                Text(
                    text = "View Inventory",
                    fontSize = 20.sp
                )
            }
            Button(
                onClick = {
                    navController.navigate(route = Screen.Catalog.route)
                },
                modifier = Modifier
                    .size(300.dp, 75.dp)
            ) {
                Text(
                    text = "Access Catalog",
                    fontSize = 20.sp
                )
            }
            Button(
                onClick = {
                    navController.navigate(route = Screen.Sales.route)
                },
                modifier = Modifier
                    .size(300.dp, 75.dp)
            ) {
                Text(
                    text = "View Sales",
                    fontSize = 20.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    VendorFlowTheme {
        HomeScreen(
            navController = rememberNavController(),
        )
    }
}