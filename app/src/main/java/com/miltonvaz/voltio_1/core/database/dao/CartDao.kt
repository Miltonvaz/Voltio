package com.miltonvaz.voltio_1.core.database.dao

import androidx.room.*
import com.miltonvaz.voltio_1.core.database.entities.CartEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Query("SELECT * FROM cart_table")
    fun getAllCartItems(): Flow<List<CartEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: CartEntity)

    @Delete
    suspend fun deleteItem(item: CartEntity)

    @Query("DELETE FROM cart_table WHERE productId = :productId")
    suspend fun deleteById(productId: Int)

    @Query("UPDATE cart_table SET quantity = :quantity WHERE productId = :productId")
    suspend fun updateQuantity(productId: Int, quantity: Int)

    @Query("DELETE FROM cart_table")
    suspend fun clearCart()
}
