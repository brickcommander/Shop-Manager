package com.brickcommander.shop.repository

import androidx.lifecycle.LiveData
import com.brickcommander.shop.db.AppDatabase

interface Repository<T>{
    abstract fun add(item: T)
    abstract fun update(item: T)
    abstract fun delete(item: T)
    abstract fun getAll(): LiveData<List<T>>
    abstract fun search(query: String?): LiveData<List<T>>
}