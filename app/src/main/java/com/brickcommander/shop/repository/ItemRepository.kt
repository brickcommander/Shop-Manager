package com.brickcommander.shop.repository

import com.brickcommander.shop.db.AppDatabase
import com.brickcommander.shop.model.Item
import com.brickcommander.shop.util.coroutineAspect

class ItemRepository(private val db: AppDatabase) {
    fun addItem(item: Item) = coroutineAspect {
        db.getItemDao().addItem(item)
    }
    fun updateItem(item: Item) = coroutineAspect {
        db.getItemDao().updateItem(item)
    }
    fun deleteItem(item: Item) = coroutineAspect {
        db.getItemDao().deleteItem(item)
    }
    fun getAllItems() = db.getItemDao().getAllItems()
    fun searchItem(query: String?) = db.getItemDao().searchItem(query)
}