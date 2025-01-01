package com.brickcommander.shop.util

import com.brickcommander.shop.model.helperModel.ItemDetail
import java.text.SimpleDateFormat
import java.util.*

// val QUANTITY: List<String> = listOf("None", "pcs", "KG", "g", "L", "ml")
fun calculateRemainingQuantity(currQuantity: Double, currQuantityQ: Int, sellQuantity: Double, sellQuantityQ: Int): Double {
    if(currQuantityQ <= 1 || currQuantityQ == sellQuantityQ) {
        return currQuantity - sellQuantity
    }

    if(currQuantityQ <= 3) {
        if(currQuantityQ == 2) { // KG - g
            return currQuantity - sellQuantity / 1000
        } else { // g - KG
            return currQuantity - sellQuantity * 1000
        }
    }

    if(currQuantityQ <= 5) {
        if(currQuantityQ == 4) { // L - ml
            return currQuantity - sellQuantity / 1000
        } else { // ml - L
            return currQuantity - sellQuantity * 1000
        }
    }

    return 0.0
}

fun convertQuantity(sellQuantity: Double, currQuantityQ: Int, sellQuantityQ: Int): Double {
    if(currQuantityQ <= 1 || currQuantityQ == sellQuantityQ) {
        return sellQuantity
    }

    if(currQuantityQ <= 3) {
        if(currQuantityQ == 2) { // KG - g
            return sellQuantity / 1000
        } else { // g - KG
            return sellQuantity * 1000
        }
    }

    if(currQuantityQ <= 5) {
        if(currQuantityQ == 4) { // L - ml
            return sellQuantity / 1000
        } else { // ml - L
            return sellQuantity * 1000
        }
    }

    return 0.0
}

fun calculateAmount(itemsDetail: List<ItemDetail>): Double {
    var amount = 0.0
    for (itemDetail in itemsDetail) {
        amount += itemDetail.item.sellingPrice * convertQuantity(itemDetail.quantity, itemDetail.item.remainingQ, itemDetail.quantityQ)
    }
    return amount
}

fun convertLongToFormattedDate(timestamp: Long): String {
    val date = Date(timestamp)
    val formatter = SimpleDateFormat("HH:mm dd/MM/yy", Locale.getDefault())
    return formatter.format(date)
}