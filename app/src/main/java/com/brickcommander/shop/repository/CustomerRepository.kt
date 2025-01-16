package com.brickcommander.shop.repository

import androidx.lifecycle.LiveData
import com.brickcommander.shop.db.AppDatabase
import com.brickcommander.shop.model.Customer
import com.brickcommander.shop.util.coroutineAspect

class CustomerRepository(private val db: AppDatabase) : Repository<Customer> {
    override fun add(customer: Customer) = coroutineAspect {
        db.getDao().addCustomer(customer)
    }

    override fun update(customer: Customer) = coroutineAspect {
        db.getDao().updateCustomer(customer)
    }

    override fun delete(customer: Customer) = coroutineAspect {
        db.getDao().deleteCustomer(customer.customerId)
    }

    override fun getAll() = db.getDao().getAllCustomers()
    override fun getAllActive(): LiveData<List<Customer>> {
        TODO("Not yet implemented")
    }

    override fun search(query: String?) = db.getDao().searchCustomer(query)
}