package com.example.vendorflow

import android.content.Context
import androidx.room.Room
import com.example.vendorflow.data.VendorDao
import com.example.vendorflow.data.VendorDatabase
import com.example.vendorflow.data.VendorRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    private lateinit var vendorDatabase: VendorDatabase
    @Singleton
    @Provides
    fun provideDatabase(
        context: Context
    ): VendorDatabase {
        vendorDatabase = Room
            .databaseBuilder(
                context = context,
                klass = VendorDatabase::class.java,
                name = "Vendor.db"
            )
            .fallbackToDestructiveMigrationOnDowngrade()
            .fallbackToDestructiveMigration()
            .build()
        return vendorDatabase
    }
    @Singleton
    @Provides
    fun provideDao(
        database: VendorDatabase
    ) = database.vendorDao

    @Singleton
    @Provides
    fun provideRepository(
        dao: VendorDao
    ) = VendorRepository(dao)
}
