package com.brickcommander.shop.repository

import androidx.lifecycle.LiveData

interface Repository<T> {
    fun add(item: T)
    fun update(item: T)
    fun delete(item: T)
    fun getAll(): LiveData<List<T>>
    fun getAllActive(): LiveData<List<T>>
    fun getAllPurchasesBuCustomerId(customerId: Long): List<T>
    fun search(query: String?): LiveData<List<T>>
}