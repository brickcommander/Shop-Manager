package com.brickcommander.shop.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.time.LocalDateTime

@Entity(tableName = "ProfileMaster")
@Parcelize
data class Profile(
    var shopName: String = "",
    var owner: String = "",
    var mobile: String = "",
    var email: String = "",
    var location: String = "",
    var gstin: String = ""
) : Parcelable {

    var createdDate: LocalDateTime? = LocalDateTime.now()

    @PrimaryKey(autoGenerate = true)
    var shopId: Long = System.currentTimeMillis()

    override fun toString(): String {
        return "Profile(shopId=$shopId, shopName=$shopName, owner=$owner, mobile=$mobile, email=$email, gstin=$gstin, location=$location)"
    }
}
