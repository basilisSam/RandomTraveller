package com.example.randomtraveller.flights.flight_results.di

import com.example.randomtraveller.flights.flight_results.data.FlightsRepositoryImpl
import com.example.randomtraveller.flights.flight_results.domain.repository.FlightsRepository
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
        flightsRepositoryImpl: FlightsRepositoryImpl
    ): FlightsRepository
}