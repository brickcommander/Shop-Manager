package com.brickcommander.shop.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.brickcommander.shop.model.Item

@Dao
interface ItemDao {

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
}