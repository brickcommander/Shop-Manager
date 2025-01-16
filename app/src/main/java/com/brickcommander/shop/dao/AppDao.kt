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
interface AppDao {
    companion object {
        val TAG = "PurchaseDao"
    }

    // ItemMaster
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addItem(item: Item)

    @Update
    fun updateItem(item: Item)

    // Never deleting the item to fix old purchases showing wrong data in case the item gets deleted
    @Query("UPDATE ItemMaster SET isActive = 0 WHERE itemId = :itemId")
    fun deleteItem(itemId: Long)

    @Query("SELECT * FROM ItemMaster WHERE isActive = 1 ORDER BY itemId DESC")
    fun getAllItems(): LiveData<List<Item>>

    @Query("SELECT * FROM ItemMaster WHERE isActive = 1 AND name LIKE :query")
    fun searchItem(query: String?): LiveData<List<Item>>

    @Query("SELECT * FROM ItemMaster WHERE itemId = :itemId")
    fun findItemByItemId(itemId: Long): Item?


    // CustomerMaster
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addCustomer(customer: Customer)

    @Update
    fun updateCustomer(customer: Customer)

    // Never deleting the customer to fix old purchases showing wrong data in case the customer gets deleted
    @Query("UPDATE CustomerMaster SET isActive = 0 WHERE customerId = :customerId")
    fun deleteCustomer(customerId: Long)

    @Query("SELECT * FROM CustomerMaster WHERE isActive = 1 ORDER BY customerId DESC")
    fun getAllCustomers(): LiveData<List<Customer>>

    @Query("SELECT * FROM CustomerMaster WHERE isActive = 1 AND name LIKE :query")
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

        if(purchase.items.size == 0) {
            Log.d(TAG, "Purchase has no items. Deleting Purchase.")
            deletePurchase(purchase)
            return
        }

        if((purchase.customer == null || purchase.items.size == 0) && purchase.active == false) {
            throw Exception("Complete Purchase should have customer and items.")
        }

        val currentDate = System.currentTimeMillis()
        val amount = calculateAmount(purchase.items)

        // Update PurchaseMaster
        updatePurchaseMaster(
            PurchaseMaster(
                purchaseId = purchase.purchaseId,
                currentDate,
                amount,
                purchase.customer!!.customerId,
                purchase.active,
            )
        )

        // Update PurchaseDetailMaster
        val purchaseDetailMasterList = purchase.items.map { itemDetail ->
            PurchaseDetailMaster(
                purchase.purchaseId,
                purchase.customer!!.customerId,
                itemDetail.item.itemId,
                itemDetail.item.sellingPrice,
                itemDetail.quantity,
                itemDetail.quantityQ
            )
        }
        deletePurchaseDetailMasterByPurchaseId(purchase.purchaseId) // delete all older items associated with this purchase
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
            purchase.customer!!.totalAmount += amount
            updateCustomer(purchase.customer!!)
        }

        Log.d(TAG, "Purchase added successfully ${purchase}")
    }

    @Transaction
    fun findPurchaseByPurchaseId(purchaseId: Long): Purchase? {
        val purchaseMaster: PurchaseMaster = findPurchaseMasterByPurchaseId(purchaseId) ?: return null

        val customer = findCustomerByCustomerId(purchaseMaster.customer_customerId)

        val purchaseDetailMasterList = findPurchaseDetailMasterByPurchaseId(purchaseId)
        val itemDetailList = purchaseDetailMasterList.map { purchaseDetailMaster ->
            val item = findItemByItemId(purchaseDetailMaster.item_itemId)?: Item()
            ItemDetail(
                item,
                purchaseDetailMaster.quantity,
                purchaseDetailMaster.quantityQ,
                purchaseDetailMaster.itemSellingPrice
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
        Log.d(TAG, "DeletePurchase called $purchase")
        val purchaseMaster: PurchaseMaster? = findPurchaseMasterByPurchaseId(purchase.purchaseId)
        if(purchaseMaster?.active == false) {
            throw Exception("Purchase is not active. Can only Delete active purchases.")
        }

        if (purchaseMaster != null) {
            deletePurchaseDetailMasterByPurchaseId(purchaseMaster.purchaseId)
            deletePurchaseMaster(purchaseMaster)
            Log.d(TAG, "purchasemaster deleted $purchaseMaster")
        }

        Log.d(TAG, "Purchase deleted successfully ${purchase}")
    }

    @Transaction
    fun getPurchaseId(): Long {
        return addPurchaseMaster(PurchaseMaster(0, System.currentTimeMillis(), 0.0, -1, true))
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

    // Below methods should only be used in testing for clean purposes
    @Query("DELETE FROM PurchaseMaster WHERE active = :active")
    fun deleteRowsFromPurchaseMaster(active: Boolean)

    @Query("DELETE FROM PurchaseDetailMaster WHERE purchase_purchaseId NOT IN (SELECT purchaseId FROM PurchaseMaster)")
    fun deleteAllOrpharRowsFromPurchaseDetailMaster()

    fun cleanUpActivePurchases() {
        deleteRowsFromPurchaseMaster(true)
        deleteAllOrpharRowsFromPurchaseDetailMaster()
    }

    fun cleanUpInctivePurchases() {
        deleteRowsFromPurchaseMaster(false)
        deleteAllOrpharRowsFromPurchaseDetailMaster()
    }
}
