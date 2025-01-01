package com.brickcommander.shop.model.helperModel

import android.os.Parcelable
import com.brickcommander.shop.model.Item
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ItemDetail(
    val item: Item,
    val quantity: Double,
    val quantityQ: Int
) : Parcelable {
    val sellingPrice: Double = 0.0
}
