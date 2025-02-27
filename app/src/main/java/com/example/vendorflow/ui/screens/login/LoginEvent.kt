package com.example.vendorflow.ui.screens.login

sealed interface LoginEvent {
    data class UpdateTextField(val textField: String, val text: String): LoginEvent
}