package com.example.vendorflow.ui.screens.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    onLoginEvent: (LoginEvent) -> Unit,
    loginState: LoginState,
    ) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(paddingValues = innerPadding)
                .padding(all = 10.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Welcome to Vendor Flow!",
                fontSize = 30.sp
            )
            TextField(
                value = loginState.businessIdField,
                onValueChange = {
                    onLoginEvent(
                        LoginEvent.UpdateTextField(
                        textField = "Business ID",
                        text = it
                        )
                    )
                },
                label = { Text(text = "Business ID") },
                placeholder = { Text(text = "(ex. Kandi Bunni or NōNē Apparels)") },
            )
            TextField(
                value = loginState.accessCodeField,
                onValueChange = {
                    onLoginEvent(
                        LoginEvent.UpdateTextField(
                        textField = "Access Code",
                        text = it
                        )
                    )
                },
                label = { Text(text = "Access Code") },
                placeholder = { Text(text = "(ex. 12345 or 00000)") },
            )
            Button(
                onClick = {
                    navController.navigate(route = Screen.Home.route)
                },
            ) {
                Text(
                    text = "Login",
                    fontSize = 30.sp
                )
            }
            Button(
                onClick = {},
            ) {
                Text(
                    text = "Register",
                    fontSize = 30.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    VendorFlowTheme {
        LoginScreen(
            navController = rememberNavController(),
            onLoginEvent = {  },
            loginState = LoginState()
        )
    }
}