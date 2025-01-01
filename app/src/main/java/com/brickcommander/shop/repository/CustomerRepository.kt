package com.brickcommander.shop.repository

import androidx.room.Transaction
import com.brickcommander.shop.db.AppDatabase
import com.brickcommander.shop.model.Customer
import com.brickcommander.shop.util.coroutineAspect

class CustomerRepository(private val db: AppDatabase) : Repository<Customer> {
    override fun add(customer: Customer) = coroutineAspect {
        db.getCustomerDao().addCustomer(customer)
    }
    override fun update(customer: Customer) = coroutineAspect {
        db.getCustomerDao().updateCustomer(customer)
    }
    override fun delete(customer: Customer) = coroutineAspect {
        db.getCustomerDao().deleteCustomer(customer)
    }
    override fun getAll() = db.getCustomerDao().getAllCustomers()
    override fun search(query: String?) = db.getCustomerDao().searchCustomer(query)
}