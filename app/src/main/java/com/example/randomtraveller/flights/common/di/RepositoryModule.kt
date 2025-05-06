package com.example.randomtraveller.flights.common.di

import com.example.randomtraveller.flights.common.data.SavedSearchesRepositoryImpl
import com.example.randomtraveller.flights.common.domain.repository.SavedSearchesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindFlightsRepository(
        savedSearchesRepository: SavedSearchesRepositoryImpl
    ): SavedSearchesRepository
}