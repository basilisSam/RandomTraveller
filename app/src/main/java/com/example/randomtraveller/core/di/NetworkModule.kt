package com.example.randomtraveller.core.di

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.network.okHttpClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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
    @Named("airports_api")
    fun provideOkHttpClient(okHttpClientBuilder: OkHttpClient.Builder): OkHttpClient {
        return okHttpClientBuilder.addInterceptor { chain ->
            val originalRequest = chain.request()
            val newRequest =
                originalRequest.newBuilder()
                    .header("x-api-key", "kK3dKN0Y0YFPprA4/IxSnw==Sy7ByQpxVrsPFyeA").build()
            chain.proceed(newRequest)
        }.build()
    }

    @Provides
    @Singleton
    @Named("airports_api")
    fun provideAirportsRetrofitClient(
        @Named("airports_api") okkHttpClient: OkHttpClient,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.api-ninjas.com/v1/")
            .client(okkHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
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

    @Provides
    @Singleton
    @Named("martech")
    fun provideMartechApolloClient(
        apolloClientBuilder: ApolloClient.Builder,
        okHttpClientBuilder: OkHttpClient.Builder,
    ): ApolloClient {
        return apolloClientBuilder
            .serverUrl("https://martech.skypicker.com/graphql")
            .okHttpClient(okHttpClientBuilder.build())
            .build()
    }
}
