package com.brickcommander.shop.util

import android.util.Log
import com.brickcommander.shop.model.Profile
import com.brickcommander.shop.model.Purchase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun getDefaultTemplate(): String {
    return """
        🧾 Receipt
        Date: [DD/MM/YYYY] Time: [HH:MM AM/PM]
        Customer: [Name]
        Items:
        [Items]
        Total: ₹[Total]
        Thank you!
        [Store]
        Mobile Number: [ShopMobile]
        GSTIN: [GSTIN]
    """.trimIndent()
}

fun generateReceiptMessage(profile: Profile, purchase: Purchase): String {
    val template = getDefaultTemplate()

    // Generate the "Items" section dynamically
    var idx = 0
    val itemsDetails = purchase.items.joinToString(separator = "\n") { itemDetail ->
        idx++
        "${idx}. ${itemDetail.item.name} ${itemDetail.quantity} x ₹${itemDetail.sellingPrice}/${UnitsManager.getNameById(itemDetail.item.sellingQ)} = ₹${calculateAmount(itemDetail)}"
    }

    val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val timeFormatter = SimpleDateFormat("h:mm a", Locale.getDefault())

    val date = Date(purchase.purchaseDate)

    val formattedDate = dateFormatter.format(date)
    val formattedTime = timeFormatter.format(date)

    val message = template
        .replace("[DD/MM/YYYY]", formattedDate)
        .replace("[HH:MM AM/PM]", formattedTime)
        .replace("[Name]", purchase.customer!!.name)
        .replace("[Items]", itemsDetails)
        .replace("[Total]", calculateAmount(purchase.items).toString())
        .replace("[Store]", profile.shopName)
        .replace("[ShopMobile]", profile.mobile)
        .replace("[GSTIN]", profile.gstin)

    Log.d("ReceiptMessage", "Message: $message")

    // Replace placeholders in the template
    return message
}

