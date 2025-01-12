package com.brickcommander.shop.model.helperModel

import android.os.Parcelable
import com.brickcommander.shop.model.Item
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ItemDetail(
    val item: Item,
    var quantity: Double,
    var quantityQ: Int,
    var sellingPrice: Double = 0.0
) : Parcelable
