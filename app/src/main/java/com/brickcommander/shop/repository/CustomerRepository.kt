package com.brickcommander.shop.repository

import com.brickcommander.shop.db.AppDatabase
import com.brickcommander.shop.model.Customer
import com.brickcommander.shop.util.coroutineAspect

class CustomerRepository(private val db: AppDatabase) {
    fun add(customer: Customer) = coroutineAspect {
        db.getCustomerDao().addCustomer(customer)
    }
    fun update(customer: Customer) = coroutineAspect {
        db.getCustomerDao().updateCustomer(customer)
    }
    fun delete(customer: Customer) = coroutineAspect {
        db.getCustomerDao().deleteCustomer(customer)
    }
    fun getAll() = db.getCustomerDao().getAllCustomers()
    fun search(query: String?) = db.getCustomerDao().searchCustomer(query)
}