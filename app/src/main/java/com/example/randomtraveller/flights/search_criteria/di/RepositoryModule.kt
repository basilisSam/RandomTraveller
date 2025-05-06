package com.example.randomtraveller.flights.search_criteria.di

import com.example.randomtraveller.flights.search_criteria.data.AirportsRepositoryImpl
import com.example.randomtraveller.flights.search_criteria.domain.repository.AirportsRepository
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
    abstract fun bindAirportsRepository(
        airportsRepository: AirportsRepositoryImpl
    ): AirportsRepository
}