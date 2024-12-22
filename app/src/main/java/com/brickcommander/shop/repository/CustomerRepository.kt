package com.brickcommander.shop.repository

import com.brickcommander.shop.db.AppDatabase
import com.brickcommander.shop.model.Customer
import com.brickcommander.shop.util.coroutineAspect

class CustomerRepository(private val db: AppDatabase) {
    fun addCustomer(customer: Customer) = coroutineAspect {
        db.getCustomerDao().addCustomer(customer)
    }
    fun updateCustomer(customer: Customer) = coroutineAspect {
        db.getCustomerDao().updateCustomer(customer)
    }
    fun deleteCustomer(customer: Customer) = coroutineAspect {
        db.getCustomerDao().deleteCustomer(customer)
    }
    fun getAllCustomers() = db.getCustomerDao().getAllCustomers()
    fun searchCustomer(query: String?) = db.getCustomerDao().searchCustomer(query)
}