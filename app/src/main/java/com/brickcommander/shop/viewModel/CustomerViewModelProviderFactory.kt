package com.brickcommander.shop.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.brickcommander.shop.repository.CustomerRepository

class CustomerViewModelProviderFactory(
    val app: Application,
    private val customerRepository: CustomerRepository
) : ViewModelProvider.Factory {
    init {
        Log.d("CustomerViewModelProviderFactory", "CustomerViewModelProviderFactory created")
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CustomerViewModel(app, customerRepository) as T
    }
}
