package com.brickcommander.shop

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.brickcommander.shop.db.AppDatabase
import com.brickcommander.shop.repository.ItemRepository
import com.brickcommander.shop.databinding.ActivityMainBinding
import com.brickcommander.shop.model.Customer
import com.brickcommander.shop.model.Item
import com.brickcommander.shop.model.helperModel.PurchaseLite
import com.brickcommander.shop.repository.CustomerRepository
import com.brickcommander.shop.repository.PurchaseLiteRepository
import com.brickcommander.shop.repository.PurchaseRepository
import com.brickcommander.shop.repository.Repository
import com.brickcommander.shop.viewModel.MyViewModel
import com.brickcommander.shop.viewModel.MyViewModelProviderFactory
import com.brickcommander.shop.viewModel.PurchaseViewModel
import com.brickcommander.shop.viewModel.PurchaseViewModelProviderFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    lateinit var itemViewModel: MyViewModel<Item>
    lateinit var customerViewModel: MyViewModel<Customer>
    lateinit var purchaseLiteViewModel: MyViewModel<PurchaseLite>
    lateinit var purchaseViewModel: PurchaseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        setUpViewModel()
    }

    private fun setUpViewModel() {
        // initializing Item view model
        val itemRepository: Repository<Item> = ItemRepository(AppDatabase(this))
        val itemViewModelProviderFactory = MyViewModelProviderFactory(application, itemRepository)
        itemViewModel = ViewModelProvider(this, itemViewModelProviderFactory).get("ItemViewModel", MyViewModel::class.java) as MyViewModel<Item>

        // initializing Customer view model
        val customerRepository: Repository<Customer> = CustomerRepository(AppDatabase(this))
        val customerViewModelProviderFactory = MyViewModelProviderFactory(application, customerRepository)
        customerViewModel = ViewModelProvider(this, customerViewModelProviderFactory).get("CustomerViewModel", MyViewModel::class.java) as MyViewModel<Customer>

        // initializing Purchase Lite view model
        val purchaseLiteRepository: Repository<PurchaseLite> = PurchaseLiteRepository(AppDatabase(this))
        val purchaseLiteViewModelProviderFactory = MyViewModelProviderFactory(application, purchaseLiteRepository)
        purchaseLiteViewModel = ViewModelProvider(this, purchaseLiteViewModelProviderFactory).get("PurchaseLiteViewModel", MyViewModel::class.java) as MyViewModel<PurchaseLite>

        // initializing Purchase view model
        val purchaseRepository = PurchaseRepository(AppDatabase(this))
        val purchaseViewModelProviderFactory = PurchaseViewModelProviderFactory(application, purchaseRepository)
        purchaseViewModel = ViewModelProvider(this, purchaseViewModelProviderFactory).get(PurchaseViewModel::class.java)
    }
}