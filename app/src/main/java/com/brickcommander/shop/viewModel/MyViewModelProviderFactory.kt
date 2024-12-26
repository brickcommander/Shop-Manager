package com.brickcommander.shop.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.brickcommander.shop.repository.Repository

class MyViewModelProviderFactory<X>(
    val app: Application,
    private val repository: Repository<X>
) : ViewModelProvider.Factory {
    init {
        Log.d("MyViewModelProviderFactory", "MyViewModelProviderFactory created")
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MyViewModel(app, repository) as T
    }
}
