package com.example.chatoverbluetooth.di

import com.example.chatoverbluetooth.data.chat.AndroidBTController
import com.example.chatoverbluetooth.domain.chat.BTController
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun providesBTController(btController: AndroidBTController): BTController
}