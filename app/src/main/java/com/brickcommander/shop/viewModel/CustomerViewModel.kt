package com.brickcommander.shop.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.brickcommander.shop.model.Customer
import com.brickcommander.shop.repository.CustomerRepository
import kotlinx.coroutines.launch

class CustomerViewModel(
    app: Application,
    private val customerRepository: CustomerRepository
) : AndroidViewModel(app) {

    init {
        Log.d("CustomerViewModel", "CustomerViewModel created")
    }

    fun add(customer: Customer) = viewModelScope.launch {
        customerRepository.addCustomer(customer)
    }

    fun delete(customer: Customer) = viewModelScope.launch {
        customerRepository.deleteCustomer(customer)
    }

    fun update(customer: Customer) = viewModelScope.launch {
        customerRepository.updateCustomer(customer)
    }

    fun getAll() = customerRepository.getAllCustomers()

    fun search(query: String?) = customerRepository.searchCustomer(query)
}