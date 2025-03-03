package com.brickcommander.shop.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.brickcommander.shop.dao.AppDao
import com.brickcommander.shop.model.Customer
import com.brickcommander.shop.model.Item
import com.brickcommander.shop.model.helperModel.PurchaseDetailMaster
import com.brickcommander.shop.model.helperModel.PurchaseMaster

@Database(entities = [Item::class,Customer::class,PurchaseMaster::class,PurchaseDetailMaster::class], version = 6, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getDao(): AppDao

    companion object {
        private const val DATABASE_NAME = "SHOP_APP_BY_BRICKCOMMANDER_DB"

        @Volatile
        private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }
}