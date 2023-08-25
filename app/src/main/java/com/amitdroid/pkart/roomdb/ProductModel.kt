package com.amitdroid.pkart.roomdb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductModel(
    @PrimaryKey
    val productId: String,

    @ColumnInfo("productName")
    val productName: String? = "",

    @ColumnInfo("productImg")
    val productImg: String? = "",

    @ColumnInfo("productSp")
    val productSp: String? = "",
)
