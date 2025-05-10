package com.example.vendorflow.data.room

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.DeleteColumn
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.vendorflow.data.room.entities.Collection
import com.example.vendorflow.data.room.entities.Product
import com.example.vendorflow.data.room.entities.Sale
import com.example.vendorflow.data.room.entities.relations.ProductCollection
import com.example.vendorflow.data.room.entities.relations.SoldItem

@Database(
    entities = [Product::class, Sale::class, SoldItem::class, Collection::class, ProductCollection::class],
    version = 7,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 2, to = 3),
        AutoMigration(from = 3, to = 4, spec = VendorDatabase.Migration3to4::class)
    ]
)
@TypeConverters(
    value = [Converters::class]
)
abstract class VendorDatabase: RoomDatabase() {
    abstract val vendorDao: VendorDao

    @DeleteColumn(tableName = "Product", columnName = "collectionName")
    class Migration3to4: AutoMigrationSpec

    companion object {
        val migration4to5 = object : Migration(4, 5) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `Collection` (
                        `collectionId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `collectionName` TEXT NOT NULL
                    );
                    """.trimIndent()
                )
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `ProductCollection` (
                        `productCollectionId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `productId` INTEGER NOT NULL,
                        `collectionId` INTEGER NOT NULL
                    );
                    """.trimIndent()
                )
            }
        }
        val migration5to6 = object : Migration(5, 6) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                        CREATE TABLE IF NOT EXISTS `New_Product` (
                            `productId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 
                            `productName` TEXT NOT NULL, 
                            `collectionId` INTEGER NOT NULL DEFAULT 0, 
                            `image` TEXT NOT NULL, 
                            `price` REAL NOT NULL, 
                            `cost` REAL NOT NULL, 
                            `stock` INTEGER NOT NULL 
                        );
                    """.trimIndent()
                )
                db.execSQL(
                    """
                        INSERT INTO `New_Product` (`productName`, `collectionId`, `image`, `price`, `cost`, `stock`) 
                        SELECT `productName`, `collectionId`, `image`, `price`, `cost`, `stock` FROM `Product`;
                    """.trimIndent()
                )
                db.execSQL(
                    """
                        DROP TABLE `Product`;
                    """.trimIndent()
                )
                db.execSQL(
                    """
                        ALTER TABLE `New_Product` RENAME TO `Product`;
                    """.trimIndent()
                )
            }
        }
        val migration6to7 = object : Migration(6, 7) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                        CREATE TABLE IF NOT EXISTS `New_Product` (
                            `productId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 
                            `productName` TEXT NOT NULL, 
                            `collectionId` INTEGER NOT NULL DEFAULT 0, 
                            `image` TEXT NOT NULL, 
                            `price` REAL NOT NULL, 
                            `cost` REAL NOT NULL, 
                            `stock` INTEGER NOT NULL 
                        );
                    """.trimIndent()
                )
                db.execSQL(
                    """
                        INSERT INTO `New_Product` (`productName`, `collectionId`, `image`, `price`, `cost`, `stock`) 
                        SELECT `productName`, `collectionId`, `image`, `price`, `cost`, `stock` FROM `Product`;
                    """.trimIndent()
                )
                db.execSQL(
                    """
                        DROP TABLE `Product`;
                    """.trimIndent()
                )
                db.execSQL(
                    """
                        ALTER TABLE `New_Product` RENAME TO `Product`;
                    """.trimIndent()
                )
            }
        }
    }
}