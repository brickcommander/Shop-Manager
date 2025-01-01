package com.brickcommander.shop.model

import com.brickcommander.shop.model.helperModel.ItemDetail

data class Purchase(
    val items: List<ItemDetail>,
    val customer: Customer,
    val active: Boolean,
    val purchaseDate: Long,
    val totalAmount: Double,
    val purchaseId: Long
) {
    val paidAmount: Double = 0.0
}
