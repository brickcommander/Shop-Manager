package com.brickcommander.shop.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(
    tableName = "CustomerMaster",
    indices = [Index(value = ["isActive", "name"])]
)
@Parcelize
data class Customer(
    var name: String = "NotDefined",
    var mobile: String = "",
    var email: String = "",
    var address: String = "",
    var customerNameQ: Int = 0
) : Parcelable {

    var createdDate: Long = System.currentTimeMillis()
    var dueAmount: Double = 0.0
    var totalAmount: Double = 0.0
    var isActive: Boolean = true

    @PrimaryKey(autoGenerate = true)
    var customerId: Long = 0

    override fun toString(): String {
        return "Customer(customerId=$customerId, name=$name, mobile=$mobile, email=$email, address=$address, customerNameQ=$customerNameQ, dueAmount=$dueAmount, totalAmount=$totalAmount)"
    }
}
