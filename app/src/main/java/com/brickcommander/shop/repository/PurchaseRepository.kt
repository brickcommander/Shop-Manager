package com.brickcommander.shop.repository

import com.brickcommander.shop.db.AppDatabase
import com.brickcommander.shop.model.Purchase
import com.brickcommander.shop.util.coroutineAspect
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class PurchaseRepository(private val db: AppDatabase) {
    fun add(purchase: Purchase) = coroutineAspect {
        db.getDao().addPurchase(purchase)
    }

    fun update(purchase: Purchase) = coroutineAspect {
        db.getDao().updatePurchase(purchase)
    }

    fun delete(purchase: Purchase) = coroutineAspect {
        db.getDao().deletePurchase(purchase)
    }

    fun getPurchaseId(): Long = coroutineAspect {
        suspendCoroutine { continuation ->
            val purchaseId = db.getDao().getPurchaseId()
            continuation.resume(purchaseId)
        }
    }

    fun findPurchaseByPurchaseId(purchaseId: Long): Purchase? = coroutineAspect {
        suspendCoroutine { continuation ->
            val purchase = db.getDao().findPurchaseByPurchaseId(purchaseId)
            continuation.resume(purchase)
        }
    }
}