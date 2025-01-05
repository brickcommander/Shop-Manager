package com.brickcommander.shop.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.brickcommander.shop.repository.Repository
import kotlinx.coroutines.launch

class MyViewModel<T>(
    app: Application,
    private val repository: Repository<T>
) : AndroidViewModel(app) {

    init {
        Log.d("MyViewModel", "MyViewModel created")
    }

    fun add(customer: T) = viewModelScope.launch {
        repository.add(customer)
    }

    fun delete(customer: T) = viewModelScope.launch {
        repository.delete(customer)
    }

    fun update(customer: T) = viewModelScope.launch {
        repository.update(customer)
    }

    fun getAll() = repository.getAll()

    fun getAllActive() = repository.getAllActive()

    fun search(query: String?) = repository.search(query)
}