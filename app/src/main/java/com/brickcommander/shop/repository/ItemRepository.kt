package com.brickcommander.shop.repository

import com.brickcommander.shop.db.AppDatabase
import com.brickcommander.shop.model.Item
import com.brickcommander.shop.util.coroutineAspect

class ItemRepository(private val db: AppDatabase) : Repository<Item> {
    override fun add(item: Item) = coroutineAspect {
        db.getItemDao().addItem(item)
    }
    override fun update(item: Item) = coroutineAspect {
        db.getItemDao().updateItem(item)
    }
    override fun delete(item: Item) = coroutineAspect {
        db.getItemDao().deleteItem(item)
    }

    override fun getAll() = db.getItemDao().getAllItems()
    override fun search(query: String?) = db.getItemDao().searchItem(query)
}