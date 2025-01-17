package com.brickcommander.shop

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.SmsManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.brickcommander.shop.db.AppDatabase
import com.brickcommander.shop.repository.ItemRepository
import com.brickcommander.shop.databinding.ActivityMainBinding
import com.brickcommander.shop.model.Customer
import com.brickcommander.shop.model.Item
import com.brickcommander.shop.model.Profile
import com.brickcommander.shop.model.Purchase
import com.brickcommander.shop.model.helperModel.PurchaseLite
import com.brickcommander.shop.repository.CustomerRepository
import com.brickcommander.shop.repository.PurchaseLiteRepository
import com.brickcommander.shop.repository.PurchaseRepository
import com.brickcommander.shop.repository.Repository
import com.brickcommander.shop.test.Test
import com.brickcommander.shop.util.generateReceiptMessage
import com.brickcommander.shop.util.toast
import com.brickcommander.shop.viewModel.MyViewModel
import com.brickcommander.shop.viewModel.MyViewModelProviderFactory
import com.brickcommander.shop.viewModel.PurchaseViewModel
import com.brickcommander.shop.viewModel.PurchaseViewModelProviderFactory
import getObjectFromPreferences

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    lateinit var itemViewModel: MyViewModel<Item>
    lateinit var customerViewModel: MyViewModel<Customer>
    lateinit var purchaseLiteViewModel: MyViewModel<PurchaseLite>
    lateinit var purchaseViewModel: PurchaseViewModel
    var profile: Profile? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        setUpViewModel()

        checkProfileIsAvailable()

//        Test.generateItems(10000).forEach { item ->
//            itemViewModel.add(item)
//        }
//
//        Test.generateCustomers(10000).forEach { customer ->
//            customerViewModel.add(customer)
//        }
    }

    private fun checkProfileIsAvailable() {
        profile = getObjectFromPreferences(this)
        if (profile == null) {
            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
            val navController = navHostFragment.navController
            val navInflater = navController.navInflater
            val navGraph =
                navInflater.inflate(R.navigation.nav_graph) // Replace with your nav_graph ID

            navGraph.setStartDestination(R.id.addEditProfileFragment2) // Set AddEditProfileFragment as start destination
            navController.graph = navGraph
        }
    }

    private fun setUpViewModel() {
        // initializing Item view model
        val itemRepository: Repository<Item> = ItemRepository(AppDatabase(this))
        val itemViewModelProviderFactory = MyViewModelProviderFactory(application, itemRepository)
        itemViewModel = ViewModelProvider(this, itemViewModelProviderFactory).get(
            "ItemViewModel",
            MyViewModel::class.java
        ) as MyViewModel<Item>

        // initializing Customer view model
        val customerRepository: Repository<Customer> = CustomerRepository(AppDatabase(this))
        val customerViewModelProviderFactory =
            MyViewModelProviderFactory(application, customerRepository)
        customerViewModel = ViewModelProvider(this, customerViewModelProviderFactory).get(
            "CustomerViewModel",
            MyViewModel::class.java
        ) as MyViewModel<Customer>

        // initializing Purchase Lite view model
        val purchaseLiteRepository: Repository<PurchaseLite> =
            PurchaseLiteRepository(AppDatabase(this))
        val purchaseLiteViewModelProviderFactory =
            MyViewModelProviderFactory(application, purchaseLiteRepository)
        purchaseLiteViewModel = ViewModelProvider(
            this,
            purchaseLiteViewModelProviderFactory
        ).get("PurchaseLiteViewModel", MyViewModel::class.java) as MyViewModel<PurchaseLite>

        // initializing Purchase view model
        val purchaseRepository = PurchaseRepository(AppDatabase(this))
        val purchaseViewModelProviderFactory =
            PurchaseViewModelProviderFactory(application, purchaseRepository)
        purchaseViewModel = ViewModelProvider(
            this,
            purchaseViewModelProviderFactory
        ).get(PurchaseViewModel::class.java)
    }

    private val SMS_PERMISSION_CODE = 100

    private fun hasSmsPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.SEND_SMS
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestSmsPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.SEND_SMS),
            SMS_PERMISSION_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == SMS_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                toast("SMS permission granted")
            } else {
                toast("SMS permission denied")
            }
        }
    }

    fun sendSms(purchase: Purchase) {
        if (!hasSmsPermission()) {
            requestSmsPermission()
        } else {
            try {
                val message = generateReceiptMessage(profile!!, purchase)
                val smsManager = SmsManager.getDefault()

                if (message.length > 140) {
                    val parts = smsManager.divideMessage(message)
                    smsManager.sendMultipartTextMessage(
                        purchase.customer!!.mobile,
                        null,
                        parts,
                        null,
                        null
                    )
                } else {
                    smsManager.sendTextMessage(
                        purchase.customer!!.mobile,
                        null,
                        message,
                        null,
                        null
                    )
                }

                toast("Receipt Sent")
            } catch (e: Exception) {
                e.printStackTrace()
                toast("Failed to send message")
            }
        }
    }
}