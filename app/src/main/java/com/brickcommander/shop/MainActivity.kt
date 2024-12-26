package com.brickcommander.shop

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.brickcommander.shop.db.AppDatabase
import com.brickcommander.shop.repository.ItemRepository
import com.brickcommander.shop.databinding.ActivityMainBinding
import com.brickcommander.shop.model.Customer
import com.brickcommander.shop.model.Item
import com.brickcommander.shop.repository.CustomerRepository
import com.brickcommander.shop.repository.Repository
import com.brickcommander.shop.viewModel.MyViewModel
import com.brickcommander.shop.viewModel.MyViewModelProviderFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    lateinit var itemViewModel: MyViewModel<Item>
    lateinit var customerViewModel: MyViewModel<Customer>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        setUpViewModel()
    }

    private fun setUpViewModel() {
        // initializing item view model
        val itemRepository: Repository<Item> = ItemRepository(AppDatabase(this))
        val itemViewModelProviderFactory = MyViewModelProviderFactory(application, itemRepository)
        itemViewModel = ViewModelProvider(this, itemViewModelProviderFactory).get("ItemViewModel", MyViewModel::class.java) as MyViewModel<Item>

        // initializing customer view model
        val customerRepository: Repository<Customer> = CustomerRepository(AppDatabase(this))
        val customerViewModelProviderFactory = MyViewModelProviderFactory(application, customerRepository)
        customerViewModel = ViewModelProvider(this, customerViewModelProviderFactory).get("CustomerViewModel", MyViewModel::class.java) as MyViewModel<Customer>
    }
}