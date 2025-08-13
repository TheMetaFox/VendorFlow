package com.example.vendorflow.data.room

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.DeleteColumn
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.vendorflow.data.room.entities.Product
import com.example.vendorflow.data.room.entities.Sale
import com.example.vendorflow.data.room.entities.Tag
import com.example.vendorflow.data.room.entities.relations.ProductTag
import com.example.vendorflow.data.room.entities.relations.SoldItem

@Database(
    entities = [Product::class, Tag::class, Sale::class, ProductTag::class, SoldItem::class],
    version = 8,
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
        val migration4to5 = object : Migration(startVersion = 4, endVersion = 5) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    sql = """
                    CREATE TABLE IF NOT EXISTS `Collection` (
                        `collectionId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `collectionName` TEXT NOT NULL
                    );
                    """.trimIndent()
                )
                db.execSQL(
                    sql = """
                    CREATE TABLE IF NOT EXISTS `ProductCollection` (
                        `productCollectionId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `productId` INTEGER NOT NULL,
                        `collectionId` INTEGER NOT NULL
                    );
                    """.trimIndent()
                )
            }
        }
        val migration5to6 = object : Migration(startVersion = 5, endVersion = 6) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    sql = """
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
                    sql = """
                        INSERT INTO `New_Product` (`productName`, `collectionId`, `image`, `price`, `cost`, `stock`) 
                        SELECT `productName`, `collectionId`, `image`, `price`, `cost`, `stock` FROM `Product`;
                    """.trimIndent()
                )
                db.execSQL(
                    sql = """
                        DROP TABLE `Product`;
                    """.trimIndent()
                )
                db.execSQL(
                    sql = """
                        ALTER TABLE `New_Product` RENAME TO `Product`;
                    """.trimIndent()
                )
            }
        }
        val migration6to7 = object : Migration(startVersion = 6, endVersion = 7) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    sql = """
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
                    sql = """
                        INSERT INTO `New_Product` (`productName`, `collectionId`, `image`, `price`, `cost`, `stock`) 
                        SELECT `productName`, `collectionId`, `image`, `price`, `cost`, `stock` FROM `Product`;
                    """.trimIndent()
                )
                db.execSQL(
                    sql = """
                        DROP TABLE `Product`;
                    """.trimIndent()
                )
                db.execSQL(
                    sql = """
                        ALTER TABLE `New_Product` RENAME TO `Product`;
                    """.trimIndent()
                )
            }
        }
        val migration7to8 = object : Migration(startVersion = 7, endVersion = 8) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    sql = """
                        DROP TABLE `Collection`;
                    """.trimIndent()
                )
                db.execSQL(
                    sql = """
                        DROP TABLE `ProductCollection`;
                    """.trimIndent()
                )
                db.execSQL(
                    sql = """
                        CREATE TABLE IF NOT EXISTS `Tag` (
                            `tagId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 
                            `tagName` TEXT NOT NULL, 
                            `ordinal` INTEGER NOT NULL 
                        );
                    """.trimIndent(),
                )
                db.execSQL(
                    sql = """
                        CREATE TABLE IF NOT EXISTS `ProductTag` (
                            `productTagId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                            `productId` INTEGER NOT NULL,
                            `tagId` INTEGER NOT NULL
                        );
                    """.trimIndent(),
                )
                db.execSQL(
                    sql = """
                        CREATE TABLE IF NOT EXISTS `New_Product` (
                            `productId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 
                            `productName` TEXT NOT NULL, 
                            `image` TEXT NOT NULL, 
                            `price` REAL NOT NULL, 
                            `cost` REAL NOT NULL, 
                            `stock` INTEGER NOT NULL 
                        );
                    """.trimIndent()
                )
                db.execSQL(
                    sql = """
                        INSERT INTO `New_Product` (`productName`, `image`, `price`, `cost`, `stock`) 
                        SELECT `productName`, `image`, `price`, `cost`, `stock` FROM `Product`;
                    """.trimIndent()
                )
                db.execSQL(
                    sql = """
                        DROP TABLE `Product`;
                    """.trimIndent()
                )
                db.execSQL(
                    sql = """
                        ALTER TABLE `New_Product` RENAME TO `Product`;
                    """.trimIndent()
                )
            }
        }
    }
}