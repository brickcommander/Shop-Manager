package com.brickcommander.shop.model.helperModel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PurchaseLite(
    val purchaseId: Long,
    val purchaseDate: Long,
    val totalAmount: Double,
    val customerName: String,
    val active: Boolean
) : Parcelable
