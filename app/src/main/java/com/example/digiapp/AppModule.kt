package com.example.digiapp

import com.example.digiapp.data.networks.ApiService
import com.example.digiapp.data.networks.RetrofitClient
import com.example.digiapp.data.repositories.QuestionsRepository
import com.example.digiapp.data.repositories.SeriesRepository
import com.example.digiapp.data.repositories.SongRepository
import com.example.digiapp.data.repositories.TamersRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): RetrofitClient {
        return RetrofitClient()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofitClient: RetrofitClient) : ApiService {
        return retrofitClient.getRetrofit().create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideQuestionsRepository(apiService: ApiService): QuestionsRepository {
        return QuestionsRepository(apiService)
    }

    @Provides
    @Singleton
    fun provideSeriesRepository(apiService: ApiService): SeriesRepository {
        return SeriesRepository(apiService)
    }

    @Provides
    @Singleton
    fun provideSongsRepository(apiService: ApiService): SongRepository {
        return SongRepository(apiService)
    }

    @Provides
    @Singleton
    fun provideTamersRepository(apiService: ApiService): TamersRepository {
        return TamersRepository(apiService)
    }
}