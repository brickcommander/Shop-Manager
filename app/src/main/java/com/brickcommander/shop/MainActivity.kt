package com.brickcommander.shop

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.brickcommander.shop.db.AppDatabase
import com.brickcommander.shop.repository.ItemRepository
import com.brickcommander.shop.viewModel.ItemViewModel
import com.brickcommander.shop.viewModel.ItemViewModelProviderFactory
import com.brickcommander.shop.databinding.ActivityMainBinding
import com.brickcommander.shop.repository.CustomerRepository
import com.brickcommander.shop.viewModel.CustomerViewModel
import com.brickcommander.shop.viewModel.CustomerViewModelProviderFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    lateinit var itemViewModel: ItemViewModel
    lateinit var customerViewModel: CustomerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        setUpViewModel()
    }

    private fun setUpViewModel() {
        // initializing item view model
        val itemRepository = ItemRepository(
            AppDatabase(this)
        )
        val itemViewModelProviderFactory =
            ItemViewModelProviderFactory(
                application,
                itemRepository
            )
        itemViewModel = ViewModelProvider(
            this,
            itemViewModelProviderFactory
        ).get(ItemViewModel::class.java)

        // initializing customer view model
        val customerRepository = CustomerRepository(
            AppDatabase(this)
        )
        val customerViewModelProviderFactory =
            CustomerViewModelProviderFactory(
                application,
                customerRepository
            )
        customerViewModel = ViewModelProvider(
            this,
            customerViewModelProviderFactory
        ).get(CustomerViewModel::class.java)
    }
}