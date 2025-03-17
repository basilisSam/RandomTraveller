package com.example.randomtraveller.flights.di

import com.example.randomtraveller.flights.data.service.AirportService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AirportsServiceModule {

    @Provides
    @Singleton
    fun provideAirportsService(@Named("airports_api") retrofit: Retrofit): AirportService {
        return retrofit.create(AirportService::class.java)
    }
}