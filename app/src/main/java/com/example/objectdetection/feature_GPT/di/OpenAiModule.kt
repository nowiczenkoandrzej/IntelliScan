package com.example.objectdetection.feature_GPT.di

import com.aallam.openai.client.OpenAI
import com.example.objectdetection.BuildConfig
import com.example.objectdetection.feature_GPT.data.repository.GPTRepositoryImpl
import com.example.objectdetection.feature_GPT.domain.repository.GPTRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object OpenAiModule {

    @Provides
    @Singleton
    fun provideOpenAIApiKey(): String = BuildConfig.GPT_API_KEY

    @Provides
    @Singleton
    fun provideOpenAiClient(api_key: String): OpenAI = OpenAI(api_key)
    
}

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @ViewModelScoped
    @Binds
    abstract fun bindGPTRepository(
        repository: GPTRepositoryImpl
    ): GPTRepository

}