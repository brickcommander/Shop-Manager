package com.brickcommander.shop.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.time.LocalDateTime

@Entity(tableName = "CustomerMaster")
@Parcelize
data class Customer(
    var name: String = "NotDefined",
    var mobile: String = "",
    var email: String = "",
    var address: String = "",
    var customerNameQ: Int = 0
) : Parcelable {

    var createdDate: LocalDateTime? = LocalDateTime.now()
    var dueAmount: Double = 0.0

    @PrimaryKey(autoGenerate = true)
    var customerId: Long = System.currentTimeMillis()

    override fun toString(): String {
        return "Customer(customerId=$customerId, name=$name, mobile=$mobile, email=$email, address=$address, customerNameQ=$customerNameQ, dueAmount=$dueAmount)"
    }
}
