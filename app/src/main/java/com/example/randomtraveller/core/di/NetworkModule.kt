package com.example.randomtraveller.core.di

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.network.okHttpClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideOkHttpClientBuilder(): OkHttpClient.Builder {
        val loggingInterceptor =
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
    }

    @Provides
    @Singleton
    fun provideApolloClientBuilder(): ApolloClient.Builder {
        return ApolloClient.Builder()
    }

    @Provides
    @Singleton
    @Named("umbrella")
    fun provideUmbrellaApolloClient(
        apolloClientBuilder: ApolloClient.Builder,
        okHttpClientBuilder: OkHttpClient.Builder,
    ): ApolloClient {
        return apolloClientBuilder
            .serverUrl("https://api.skypicker.com/umbrella/v2/graphql")
            .okHttpClient(okHttpClientBuilder.build())
            .build()
    }
}
