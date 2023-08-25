package com.amitdroid.pkart.roomdb

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insertProduct(product: ProductModel)

    @Delete
     fun deleteProduct(product: ProductModel)

    @Query("Select * from products")
    fun getAllProducts() : LiveData<List<ProductModel>>

    @Query("Select * from products WHERE productId = :id")
    fun isExit(id: String) :ProductModel
}