package com.example.vendorflow.ui.screens.sales

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vendorflow.data.VendorRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SalesViewModel(private val vendorRepository: VendorRepository): ViewModel() {

    private val _state: MutableStateFlow<SalesState> = MutableStateFlow(value = SalesState())
    val state: StateFlow<SalesState> = _state

    fun onEvent(salesEvent: SalesEvent) {
        when (salesEvent) {
            is SalesEvent.LoadSalesData -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            salesList = vendorRepository.getSalesOrderedByDateTime(),
                            soldItemsList = vendorRepository.getSoldItemsGroupedBySaleOrderedByDateTime()
                        )
                    }
                }
            }
        }
    }
}