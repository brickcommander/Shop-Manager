package com.brickcommander.shop.model.helperModel

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Index

@Entity(
    tableName = "PurchaseMaster",
    indices = [Index(value = ["purchaseDate"]), Index(value = ["active"])]
)
data class PurchaseMaster(
    @PrimaryKey(autoGenerate = true)
    var purchaseId: Long = 0,
    val purchaseDate: Long,
    val totalAmount: Double,
    val customer_customerId: Long,
    val active: Boolean
)