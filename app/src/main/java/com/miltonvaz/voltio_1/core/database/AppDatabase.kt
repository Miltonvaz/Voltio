package com.miltonvaz.voltio_1.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.miltonvaz.voltio_1.core.database.dao.CartDao
import com.miltonvaz.voltio_1.core.database.entities.CartEntity

@Database(entities = [CartEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao
}
