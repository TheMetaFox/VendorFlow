package com.example.vendorflow

import android.content.Context
import androidx.room.Room
import com.example.vendorflow.data.room.VendorDao
import com.example.vendorflow.data.room.VendorDatabase
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
            .addMigrations(VendorDatabase.migration4to5, VendorDatabase.migration5to6, VendorDatabase.migration6to7, VendorDatabase.migration7to8)
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
    fun provideDatabaseRepository(
        dao: VendorDao
    ) = VendorRepository(dao)
}
