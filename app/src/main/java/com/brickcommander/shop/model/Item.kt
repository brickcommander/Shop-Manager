package com.brickcommander.shop.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "ItemMaster")
@Parcelize
data class Item(
    var name: String = "NotDefined",
    var buyingPrice: Double = 0.0,
    var sellingPrice: Double = 0.0,
    var totalCount: Double = 0.0,
    var remainingCount: Double = 0.0,
    var rateQ: Int = 0,
    var totalQ: Int = 0,
    var remainingQ: Int = 0
) : Parcelable {

    var createdDate: Long = System.currentTimeMillis()

    @PrimaryKey(autoGenerate = true)
    var itemId: Long = 0

    // Optional methods or computed properties if needed
    fun getProfit(): Double {
        return sellingPrice - buyingPrice
    }

    fun isAvailable(): Boolean {
        return remainingCount > 0
    }

    fun copy(): Item {
        val item = Item(name, buyingPrice, sellingPrice, totalCount, remainingCount, rateQ, totalQ, remainingQ)
        item.createdDate = createdDate
        item.itemId = itemId
        return item
    }

    override fun toString(): String {
        return "Item(Name: $name, Buying Price: $buyingPrice, Selling Price: $sellingPrice, Available: $remainingCount, Total Count: $totalCount, Created Date: $createdDate)"
    }
}
