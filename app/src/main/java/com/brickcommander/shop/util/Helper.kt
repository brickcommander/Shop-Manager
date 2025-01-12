package com.brickcommander.shop.util

import com.brickcommander.shop.model.helperModel.ItemDetail
import com.brickcommander.shop.shared.CONSTANTS
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

//fun convertQuantity(sellQuantity: Double, currQuantityQ: Int, sellQuantityQ: Int): Double {
//    if(currQuantityQ <= 1 || currQuantityQ == sellQuantityQ) {
//        return sellQuantity
//    }
//
//    if(currQuantityQ <= 3) {
//        if(currQuantityQ == 2) { // KG - g
//            return sellQuantity / 1000
//        } else { // g - KG
//            return sellQuantity * 1000
//        }
//    }
//
//    if(currQuantityQ <= 5) {
//        if(currQuantityQ == 4) { // L - ml
//            return sellQuantity / 1000
//        } else { // ml - L
//            return sellQuantity * 1000
//        }
//    }
//
//    return 0.0
//}

fun calculateAmount(itemsDetail: List<ItemDetail>): Double {
    var amount = 0.0
    for (itemDetail in itemsDetail) {
        amount += calculateAmount(itemDetail)
    }
    return amount
}

fun calculateAmount(itemDetail: ItemDetail): Double {
    return itemDetail.item.sellingPrice * UnitsManager.convert(itemDetail.quantity, itemDetail.item.sellingQ, itemDetail.quantityQ)
}

fun convertLongToFormattedDate(timestamp: Long): String {
    val date = Date(timestamp)
    val formatter = SimpleDateFormat("hh:mm a dd/MM/yy", Locale.getDefault())
    return formatter.format(date).uppercase()
}

fun findPositionFromUnitId(unitNames: List<String>, unitId: Int): Int {
    unitNames.forEachIndexed { index, unitName ->
        if (UnitsManager.getUnitIdByName(unitName) == unitId) {
            return index
        }
    }
    return 0
}

//fun getSpinnerListByCurrentQuantityType(itemQ: Int): List<String> {
//    if(itemQ == 0) return CONSTANTS.QUANTITY
//    if(itemQ == 1) return listOf(CONSTANTS.QUANTITY[1])
//    if(itemQ <= 3) return listOf(CONSTANTS.QUANTITY[2], CONSTANTS.QUANTITY[3])
//    if(itemQ <= 5) return listOf(CONSTANTS.QUANTITY[4], CONSTANTS.QUANTITY[5])
//    return listOf(CONSTANTS.QUANTITY[0])
//}
//
//fun getItemQFromItemQString(itemQString: String): Int {
//    for(element in CONSTANTS.QUANTITY) {
//        if(element == itemQString) return CONSTANTS.QUANTITY.indexOf(element)
//    }
//    return 0
//}
