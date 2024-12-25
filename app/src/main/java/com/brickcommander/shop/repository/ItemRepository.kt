package com.brickcommander.shop.repository

import com.brickcommander.shop.db.AppDatabase
import com.brickcommander.shop.model.Item
import com.brickcommander.shop.util.coroutineAspect

class ItemRepository(private val db: AppDatabase) {
    fun add(item: Item) = coroutineAspect {
        db.getItemDao().addItem(item)
    }
    fun update(item: Item) = coroutineAspect {
        db.getItemDao().updateItem(item)
    }
    fun delete(item: Item) = coroutineAspect {
        db.getItemDao().deleteItem(item)
    }
    fun getAll() = db.getItemDao().getAllItems()
    fun search(query: String?) = db.getItemDao().searchItem(query)
}