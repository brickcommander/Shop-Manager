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

    fun add(item: Item) = viewModelScope.launch {
        itemRepository.addItem(item)
    }

    fun delete(item: Item) = viewModelScope.launch {
        itemRepository.deleteItem(item)
    }

    fun update(item: Item) = viewModelScope.launch {
        itemRepository.updateItem(item)
    }

    fun getAll() = itemRepository.getAllItems()

    fun search(query: String?) = itemRepository.searchItem(query)
}