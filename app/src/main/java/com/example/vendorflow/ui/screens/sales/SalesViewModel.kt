package com.example.vendorflow.ui.screens.sales

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vendorflow.data.VendorRepository
import com.example.vendorflow.logic.GetSalesUseCase
import com.example.vendorflow.logic.GetSoldItemsGroupedBySaleUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SalesViewModel(
//    private val vendorRepository: VendorRepository,
    private val getSalesUseCase: GetSalesUseCase,
    private val getSoldItemsGroupedBySaleUseCase: GetSoldItemsGroupedBySaleUseCase
): ViewModel() {

    private val _state: MutableStateFlow<SalesState> = MutableStateFlow(value = SalesState())
    val state: StateFlow<SalesState> = _state

    fun onEvent(salesEvent: SalesEvent) {
        when (salesEvent) {
            is SalesEvent.LoadSalesData -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
//                            salesList = vendorRepository.getSalesOrderedByDateTime(),
//                            soldItemsList = vendorRepository.getSoldItemsGroupedBySaleOrderedByDateTime(),
                            salesList = getSalesUseCase(),
                            soldItemsList = getSoldItemsGroupedBySaleUseCase()
                        )
                    }
                }
            }
            is SalesEvent.HideSaleDialog -> {
                _state.update {
                    it.copy(
                        isShowingSaleDialog = false,
                        sale = null,
                        soldItems = listOf()
                    )
                }
            }
            is SalesEvent.ShowSaleDialog -> {
                _state.update {
                    it.copy(
                        isShowingSaleDialog = true,
                        sale = salesEvent.sale,
                        soldItems = salesEvent.soldItems
                    )
                }
            }
        }
    }
}