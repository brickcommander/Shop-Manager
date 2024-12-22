package com.brickcommander.shop.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.brickcommander.shop.model.Customer

@Dao
interface CustomerDao {

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
}