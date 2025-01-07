package com.brickcommander.shop.model

import com.brickcommander.shop.model.helperModel.ItemDetail

data class Purchase(
    var items: List<ItemDetail>,
    var customer: Customer?,
    var active: Boolean,
    var purchaseDate: Long,
    var totalAmount: Double,
    var purchaseId: Long
) {
    val paidAmount: Double = 0.0
}
