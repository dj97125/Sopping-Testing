package com.example.shoppinglisttesting.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ShoppingItem::class],
    version = 1,
    exportSchema = false)

abstract class ShoppingItemDataBase: RoomDatabase() {
    abstract fun shoppingDao(): ShoppingDao
}