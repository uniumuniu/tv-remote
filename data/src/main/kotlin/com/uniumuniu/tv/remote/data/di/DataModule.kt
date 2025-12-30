package com.uniumuniu.tv.remote.data.di

import com.google.gson.GsonBuilder
import com.uniumuniu.tv.domain.repository.TokenRepository
import com.uniumuniu.tv.domain.repository.TvRemoteRepository
import com.uniumuniu.tv.remote.data.TokenRepositoryImpl
import com.uniumuniu.tv.remote.data.TvRemoteApi
import com.uniumuniu.tv.remote.data.TvRemoteRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    abstract fun bindTvRemoteRepository(impl: TvRemoteRepositoryImpl): TvRemoteRepository

    @Binds
    abstract fun bindTokenRepository(impl: TokenRepositoryImpl): TokenRepository

    companion object {
        const val BASE_URL = "https://a76c199c-161a-4a4d-b7b6-2cf73e715834.mock.pstmn.io"

        @Provides
        fun provideRetrofitInstance(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .build()
        }

        @Provides
        fun provideTvRemoteApi(retrofit: Retrofit): TvRemoteApi {
            return retrofit.create(TvRemoteApi::class.java)
        }
    }
}