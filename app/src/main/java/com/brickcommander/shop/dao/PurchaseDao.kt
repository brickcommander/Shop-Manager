package com.brickcommander.shop.dao

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.brickcommander.shop.model.Customer
import com.brickcommander.shop.model.Item
import com.brickcommander.shop.model.Purchase
import com.brickcommander.shop.model.helperModel.ItemDetail
import com.brickcommander.shop.model.helperModel.PurchaseDetailMaster
import com.brickcommander.shop.model.helperModel.PurchaseLite
import com.brickcommander.shop.model.helperModel.PurchaseMaster
import com.brickcommander.shop.util.calculateAmount
import com.brickcommander.shop.util.calculateRemainingQuantity

@Dao
interface PurchaseDao {
    companion object {
        val TAG = "PurchaseDao"
    }

    // ItemMaster
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addItem(item: Item)

    @Update
    fun updateItem(item: Item)

    @Delete
    fun deleteItem(item: Item)

    @Query("SELECT * FROM ItemMaster ORDER BY itemId DESC")
    fun getAllItems(): LiveData<List<Item>>

    @Query("SELECT * FROM ItemMaster WHERE name LIKE :query")
    fun searchItem(query: String?): LiveData<List<Item>>

    @Query("SELECT * FROM ItemMaster WHERE itemId = :itemId")
    fun findItemByItemId(itemId: Long): Item?


    // CustomerMaster
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addCustomer(customer: Customer)

    @Update
    fun updateCustomer(customer: Customer)

    @Delete
    fun deleteCustomer(customer: Customer)

    @Query("SELECT * FROM CustomerMaster ORDER BY customerId DESC")
    fun getAllCustomers(): LiveData<List<Customer>>

    @Query("SELECT * FROM CustomerMaster WHERE name LIKE :query")
    fun searchCustomer(query: String?): LiveData<List<Customer>>

    @Query("SELECT * FROM CustomerMaster WHERE customerId = :customerId")
    fun findCustomerByCustomerId(customerId: Long): Customer?


    // PurchaseMaster
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addPurchaseMaster(purchaseMaster: PurchaseMaster): Long

    @Update
    fun updatePurchaseMaster(purchaseMaster: PurchaseMaster)

    @Delete
    fun deletePurchaseMaster(purchaseMaster: PurchaseMaster)

    @Query("SELECT * FROM PurchaseMaster WHERE purchaseId = :purchaseId")
    fun findPurchaseMasterByPurchaseId(purchaseId: Long): PurchaseMaster?


    // PurchaseDetailMaster
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addPurchaseDetailMaster(purchaseDetailMaster: PurchaseDetailMaster)

    @Update
    fun updatePurchaseDetailMaster(purchaseDetailMaster: PurchaseDetailMaster)

    @Delete
    fun deletePurchaseDetailMaster(purchaseDetailMaster: PurchaseDetailMaster)

    @Query("DELETE FROM PurchaseDetailMaster WHERE purchase_purchaseId = :purchaseId")
    fun deletePurchaseDetailMasterByPurchaseId(purchaseId: Long)

    @Query("SELECT * FROM PurchaseDetailMaster WHERE purchase_purchaseId = :purchaseId")
    fun findPurchaseDetailMasterByPurchaseId(purchaseId: Long): List<PurchaseDetailMaster>


    // Purchase
    @Transaction
    fun addPurchase(purchase: Purchase) {
        // Check, purchase should not be active
        findPurchaseMasterByPurchaseId(purchase.purchaseId)?.let {
            if (it.active == false) {
                throw Exception("Purchase is not active. Can only update active purchases.")
            }
        }

        val currentDate = System.currentTimeMillis()
        val amount = calculateAmount(purchase.items)

        // Update PurchaseMaster
        val purchaseId = addPurchaseMaster(
            PurchaseMaster(
                currentDate,
                amount,
                purchase.customer.customerId,
                purchase.active
            )
        )

        // Update PurchaseDetailMaster
        val purchaseDetailMasterList = purchase.items.map { itemDetail ->
            PurchaseDetailMaster(
                purchaseId,
                purchase.customer.customerId,
                itemDetail.item.itemId,
                itemDetail.item.sellingPrice,
                itemDetail.quantity,
                itemDetail.quantityQ
            )
        }
        deletePurchaseDetailMasterByPurchaseId(purchaseId) // delete all older items associated with this purchase
        purchaseDetailMasterList.forEach {
            addPurchaseDetailMaster(it)
        }

        // Update CustomerMaster and ItemMaster if purchase is closing
        if(purchase.active==false) {

            // Update ItemMaster
            val itemList = purchase.items.map { itemDetail ->
                itemDetail.item.remainingCount = calculateRemainingQuantity(
                    itemDetail.item.remainingCount,
                    itemDetail.item.remainingQ,
                    itemDetail.quantity,
                    itemDetail.quantityQ
                )
                itemDetail.item
            }
            itemList.forEach {
                updateItem(it)
            }

            // Update CustomerMaster
            purchase.customer.totalAmount += amount
            updateCustomer(purchase.customer)
        }

        Log.d(TAG, "Purchase added successfully ${purchase}")
    }

    @Transaction
    fun findPurchaseByPurchaseId(purchaseId: Long): Purchase? {
        val purchaseMaster: PurchaseMaster = findPurchaseMasterByPurchaseId(purchaseId) ?: return null

        val customer = findCustomerByCustomerId(purchaseMaster.customer_customerId)?: Customer()

        val purchaseDetailMasterList = findPurchaseDetailMasterByPurchaseId(purchaseId)
        val itemDetailList = purchaseDetailMasterList.map { purchaseDetailMaster ->
            val item = findItemByItemId(purchaseDetailMaster.item_itemId)?: Item()
            ItemDetail(
                item,
                purchaseDetailMaster.quantity,
                purchaseDetailMaster.quantityQ
            )
        }

        Log.d(TAG, "findPurchaseByPurchaseId : ${customer} : ${itemDetailList}")
        return Purchase(
            itemDetailList,
            customer,
            purchaseMaster.active,
            purchaseMaster.purchaseDate,
            purchaseMaster.totalAmount,
            purchaseMaster.purchaseId
        )
    }

    fun updatePurchase(purchase: Purchase) {
        addPurchase(purchase)
    }

    @Transaction
    fun deletePurchase(purchase: Purchase) {
        // Check, purchase should not be active
        val purchaseMaster: PurchaseMaster? = findPurchaseMasterByPurchaseId(purchase.purchaseId)
        if(purchaseMaster?.active == false) {
            throw Exception("Purchase is not active. Can only Delete active purchases.")
        }

        if (purchaseMaster != null) {
            deletePurchaseDetailMasterByPurchaseId(purchaseMaster.purchaseId)
            deletePurchaseMaster(purchaseMaster)
        }

        Log.d(TAG, "Purchase deleted successfully ${purchase}")
    }


    // PurchaseLite
    @Query("""
        SELECT purchaseId, purchaseDate, P.totalAmount as totalAmount, C.name as customerName, P.active as active
        FROM PurchaseMaster P 
        JOIN CustomerMaster C ON P.customer_customerId = C.customerId 
        WHERE P.active = :active
        ORDER BY purchaseDate DESC
    """)
    fun getAllPurchases(active: Boolean): LiveData<List<PurchaseLite>>

    fun getAllInActivePurchases(): LiveData<List<PurchaseLite>> {
        return getAllPurchases(false)
    }

    fun getAllActivePurchases(): LiveData<List<PurchaseLite>> {
        return getAllPurchases(true)
    }
}
