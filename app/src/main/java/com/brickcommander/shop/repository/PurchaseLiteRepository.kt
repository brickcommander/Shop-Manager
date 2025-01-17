package com.brickcommander.shop.repository

import androidx.lifecycle.LiveData
import com.brickcommander.shop.db.AppDatabase
import com.brickcommander.shop.model.helperModel.PurchaseLite
import com.brickcommander.shop.util.coroutineAspect
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class PurchaseLiteRepository(private val db: AppDatabase) : Repository<PurchaseLite> {
    override fun add(purchase: PurchaseLite) = coroutineAspect {
        throw NotImplementedError("Not yet implemented")
    }

    override fun update(purchase: PurchaseLite) = coroutineAspect {
        throw NotImplementedError("Not yet implemented")
    }

    override fun delete(purchase: PurchaseLite) = coroutineAspect {
        throw NotImplementedError("Not yet implemented")
    }

    override fun getAll() = db.getDao().getAllInActivePurchases()

    override fun getAllActive() = db.getDao().getAllActivePurchases()

    override fun getAllPurchasesBuCustomerId(customerId: Long): List<PurchaseLite> = coroutineAspect {
        suspendCoroutine { continuation ->
            val purchaseList = db.getDao().getAllPurchasesByCustomerId(customerId)
            continuation.resume(purchaseList)
        }
    }

    override fun search(query: String?): LiveData<List<PurchaseLite>> {
        throw NotImplementedError("Not yet implemented")
    }
}