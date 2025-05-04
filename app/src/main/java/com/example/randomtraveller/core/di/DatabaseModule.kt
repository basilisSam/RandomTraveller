package com.example.randomtraveller.core.di

import android.content.Context
import androidx.room.Room
import com.example.randomtraveller.core.database.RandomTravellerDatabase
import com.example.randomtraveller.core.database.SavedSearchDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): RandomTravellerDatabase {
        return Room.databaseBuilder(
            context,
            RandomTravellerDatabase::class.java,
            "random_traveller_database"
        ).fallbackToDestructiveMigration(true).build()
    }

    @Provides
    fun provideYourDao(database: RandomTravellerDatabase): SavedSearchDao {
        return database.savedSearchDao()
    }
}