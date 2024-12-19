package com.brickcommander.shop.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.time.LocalDateTime

@Entity(tableName = "ItemMaster")
@Parcelize
data class Item(
    var name: String = "NotDefined",
    var buyingPrice: Double = 0.0,
    var sellingPrice: Double = 0.0,
    var totalCount: Int = 0,
    var remainingCount: Int = 0,
    var totalQ: Int = 0,
    var remainingQ: Int = 0
) : Parcelable {

    var createdDate: LocalDateTime? = LocalDateTime.now()
    @PrimaryKey(autoGenerate = true)
    var id: Long = System.currentTimeMillis()

    // Optional methods or computed properties if needed
    fun getProfit(): Double {
        return sellingPrice - buyingPrice
    }

    fun isAvailable(): Boolean {
        return remainingCount > 0
    }

    override fun toString(): String {
        return "Name: $name, Buying Price: $buyingPrice, Selling Price: $sellingPrice, Available: $remainingCount, Total Count: $totalCount, Created Date: $createdDate"
    }
}
