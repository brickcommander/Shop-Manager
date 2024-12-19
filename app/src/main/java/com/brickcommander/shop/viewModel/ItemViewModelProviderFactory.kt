package com.brickcommander.shop.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.brickcommander.shop.repository.ItemRepository

class ItemViewModelProviderFactory(
    val app: Application,
    private val itemRepository: ItemRepository
) : ViewModelProvider.Factory {
    init {
        Log.d("ItemViewModelProviderFactory", "ItemViewModelProviderFactory created")
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ItemViewModel(app, itemRepository) as T
    }
}
