package com.miltonvaz.voltio1.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.miltonvaz.voltio1.core.database.dao.CartDao
import com.miltonvaz.voltio1.core.database.entities.CartEntity

@Database(entities = [CartEntity::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE cart_table ADD COLUMN companyId INTEGER DEFAULT NULL")
            }
        }
    }
}
