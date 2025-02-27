package com.example.vendorflow.ui.screens.login

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class LoginViewModel: ViewModel() {

    private val _state: MutableStateFlow<LoginState> = MutableStateFlow(value = LoginState())
    val state: StateFlow<LoginState> = _state

    fun onEvent(loginEvent: LoginEvent) {
        when (loginEvent) {
            is LoginEvent.UpdateTextField -> {
                when (loginEvent.textField) {
                    "Business ID" -> {
                        _state.update { loginState ->
                            loginState.copy(businessIdField = loginEvent.text)
                        }
                    }
                    "Access Code" -> {
                        _state.update { loginState ->
                            loginState.copy(accessCodeField = loginEvent.text)
                        }
                    }
                }
            }
        }
    }
}