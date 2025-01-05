package com.brickcommander.shop.repository

import androidx.lifecycle.LiveData
import com.brickcommander.shop.db.AppDatabase
import com.brickcommander.shop.model.helperModel.PurchaseLite
import com.brickcommander.shop.util.coroutineAspect

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
    override fun getAll() = db.getPurchaseDao().getAllInActivePurchases()

    override fun getAllActive() = db.getPurchaseDao().getAllActivePurchases()

    override fun search(query: String?): LiveData<List<PurchaseLite>> {
        throw NotImplementedError("Not yet implemented")
    }
}