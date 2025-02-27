package com.example.vendorflow.data

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.vendorflow.data.entities.Product
import com.example.vendorflow.data.entities.Sale
import com.example.vendorflow.data.entities.SoldItem

@Database(
    entities = [Product::class, Sale::class, SoldItem::class],
    version = 2,
    exportSchema = false,
//    autoMigrations = [AutoMigration(1, 2)]
)
@TypeConverters(
    value = [Converters::class]
)
abstract class VendorDatabase: RoomDatabase() {
    abstract val vendorDao: VendorDao
}