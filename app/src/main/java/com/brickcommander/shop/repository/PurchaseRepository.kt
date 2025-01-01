package com.brickcommander.shop.repository

import com.brickcommander.shop.db.AppDatabase
import com.brickcommander.shop.model.Purchase
import com.brickcommander.shop.util.coroutineAspect

class PurchaseRepository(private val db: AppDatabase) {
    fun add(purchase: Purchase) = coroutineAspect {
        db.getPurchaseDao().addPurchase(purchase)
    }
    fun update(purchase: Purchase) = coroutineAspect {
        db.getPurchaseDao().updatePurchase(purchase)
    }
    fun delete(purchase: Purchase) = coroutineAspect {
        db.getPurchaseDao().deletePurchase(purchase)
    }
    fun findPurchaseByPurchaseId(purchaseId: Long): Purchase? = coroutineAspect {
        return@coroutineAspect db.getPurchaseDao().findPurchaseByPurchaseId(purchaseId)
    }
}