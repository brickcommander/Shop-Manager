package com.brickcommander.shop.model.helperModel

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "PurchaseDetailMaster",
    indices = [Index(value = ["purchase_purchaseId", "customer_customerId", "item_itemId"])]
)
data class PurchaseDetailMaster(
    val purchase_purchaseId: Long,
    val customer_customerId: Long?,
    val item_itemId: Long,
    val itemSellingPrice: Double,
    val quantity: Double,
    val quantityQ: Int
) {
    @PrimaryKey(autoGenerate = true)
    var purchaseDetailId: Long = 0
}
