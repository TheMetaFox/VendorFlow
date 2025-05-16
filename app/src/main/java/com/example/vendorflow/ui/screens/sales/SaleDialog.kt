package com.example.vendorflow.ui.screens.sales

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.vendorflow.ui.theme.VendorFlowTheme

@Composable
fun SaleDialog(
    modifier: Modifier = Modifier,
    onSalesEvent: (SalesEvent) -> Unit,
    salesState: SalesState,
) {
    Dialog(
        onDismissRequest = {
            onSalesEvent(SalesEvent.HideSaleDialog)
        },
    ) {
        Box(
            modifier = modifier
                .padding(all = 10.dp)
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            Column(
                modifier = modifier,
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = salesState.sale.toString(),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = salesState.soldItems.toString(),
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SalesDialogPreview() {
    VendorFlowTheme {

        SaleDialog(
            modifier = Modifier
                .size(width = 300.dp, height = 200.dp),
            onSalesEvent = { },
            salesState = SalesState()
        )
    }
}