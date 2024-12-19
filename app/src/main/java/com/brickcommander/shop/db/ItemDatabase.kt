package com.brickcommander.shop.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.brickcommander.shop.model.Item
import com.brickcommander.shop.util.Converters

@Database(entities = [Item::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class ItemDatabase : RoomDatabase() {

    abstract fun getItemDao(): ItemDao

    companion object {

        private const val DATABASE_NAME = "items_db"

        @Volatile
        private var instance: ItemDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            ItemDatabase::class.java,
            DATABASE_NAME
        ).build()
    }
}