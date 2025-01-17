package com.brickcommander.shop.repository

import androidx.lifecycle.LiveData
import com.brickcommander.shop.db.AppDatabase
import com.brickcommander.shop.model.Item
import com.brickcommander.shop.util.coroutineAspect

class ItemRepository(private val db: AppDatabase) : Repository<Item> {
    override fun add(item: Item) = coroutineAspect {
        db.getDao().addItem(item)
    }

    override fun update(item: Item) = coroutineAspect {
        db.getDao().updateItem(item)
    }

    override fun delete(item: Item) = coroutineAspect {
        db.getDao().deleteItem(item.itemId)
    }

    override fun getAll() = db.getDao().getAllItems()
    override fun getAllActive(): LiveData<List<Item>> {
        TODO("Not yet implemented")
    }

    override fun getAllPurchasesBuCustomerId(customerId: Long): List<Item> {
        TODO("Not yet implemented")
    }

    override fun search(query: String?) = db.getDao().searchItem(query)
}