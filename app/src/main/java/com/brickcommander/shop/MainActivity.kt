package com.brickcommander.shop

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.brickcommander.shop.db.ItemDatabase
import com.brickcommander.shop.repository.ItemRepository
import com.brickcommander.shop.viewModel.ItemViewModel
import com.brickcommander.shop.viewModel.ItemViewModelProviderFactory
import com.brickcommander.shop.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    lateinit var itemViewModel: ItemViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        setUpViewModel()
    }

    private fun setUpViewModel() {
        val itemRepository = ItemRepository(
            ItemDatabase(this)
        )
        val viewModelProviderFactory =
            ItemViewModelProviderFactory(
                application,
                itemRepository
            )
        itemViewModel = ViewModelProvider(
            this,
            viewModelProviderFactory
        ).get(ItemViewModel::class.java)
    }
}