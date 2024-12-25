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
        itemRepository.add(item)
    }

    fun delete(item: Item) = viewModelScope.launch {
        itemRepository.delete(item)
    }

    fun update(item: Item) = viewModelScope.launch {
        itemRepository.update(item)
    }

    fun getAll() = itemRepository.getAll()

    fun search(query: String?) = itemRepository.search(query)
}