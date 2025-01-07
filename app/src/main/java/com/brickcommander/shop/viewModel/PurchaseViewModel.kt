package com.brickcommander.shop.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.brickcommander.shop.model.Purchase
import com.brickcommander.shop.repository.PurchaseRepository
import com.brickcommander.shop.repository.Repository
import kotlinx.coroutines.launch

class PurchaseViewModel(
    app: Application,
    private val repository: PurchaseRepository
) : AndroidViewModel(app) {

    init {
        Log.d("PurchaseViewModel", "PurchaseViewModel created")
    }

    fun add(purchase: Purchase) = viewModelScope.launch {
        repository.add(purchase)
    }

    fun delete(purchase: Purchase) = viewModelScope.launch {
        repository.delete(purchase)
    }

    fun update(purchase: Purchase) = viewModelScope.launch {
        repository.update(purchase)
    }

    fun getPurchaseId(): Long {
        return repository.getPurchaseId()
    }

    fun findPurchaseByPurchaseId(purchaseId: Long): Purchase? {
        return repository.findPurchaseByPurchaseId(purchaseId)
    }
}