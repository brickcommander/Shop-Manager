package com.brickcommander.shop.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.brickcommander.shop.repository.PurchaseLiteRepository
import com.brickcommander.shop.repository.PurchaseRepository
import com.brickcommander.shop.repository.Repository

class PurchaseViewModelProviderFactory(
    val app: Application,
    private val repository: PurchaseRepository
) : ViewModelProvider.Factory {
    init {
        Log.d("PurchaseViewModelProviderFactory", "PurchaseViewModelProviderFactory created")
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PurchaseViewModel(app, repository) as T
    }
}
