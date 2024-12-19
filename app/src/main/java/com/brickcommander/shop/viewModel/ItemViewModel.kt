package com.brickcommander.shop.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.brickcommander.shop.model.Item
import com.brickcommander.shop.repository.ItemRepository
import kotlinx.coroutines.launch

class ItemViewModel(
    app: Application,
    private val itemRepository: ItemRepository
) : AndroidViewModel(app) {

    init {
        Log.d("ItemViewModel", "ItemViewModel created")
    }

    fun addItem(item: Item) = viewModelScope.launch {
        itemRepository.addItem(item)
    }

    fun deleteItem(item: Item) = viewModelScope.launch {
        itemRepository.deleteItem(item)
    }

    fun updateItem(item: Item) = viewModelScope.launch {
        itemRepository.updateItem(item)
    }

    fun getAllItems() = itemRepository.getAllItems()

    fun searchItem(query: String?) = itemRepository.searchItem(query)
}