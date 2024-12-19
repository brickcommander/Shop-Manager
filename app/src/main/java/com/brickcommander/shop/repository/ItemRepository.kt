package com.brickcommander.shop.repository

import com.brickcommander.shop.db.ItemDatabase
import com.brickcommander.shop.model.Item

class ItemRepository(private val db: ItemDatabase) {

    suspend fun addItem(item: Item) = db.getItemDao().addItem(item)
    suspend fun updateItem(item: Item) = db.getItemDao().updateItem(item)
    suspend fun deleteItem(item: Item) = db.getItemDao().deleteItem(item)
    fun getAllItems() = db.getItemDao().getAllItems()
    fun searchItem(query: String?) = db.getItemDao().searchItem(query)
}