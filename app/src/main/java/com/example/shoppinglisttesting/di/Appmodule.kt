package com.example.shoppinglisttesting.di

import android.content.Context
import androidx.room.Room
import com.example.shoppinglisttesting.Repositories.DefaultShoppingRepository
import com.example.shoppinglisttesting.Repositories.ShoppingRepository
import com.example.shoppinglisttesting.data.local.ShoppingDao
import com.example.shoppinglisttesting.data.local.ShoppingItemDataBase
import com.example.shoppinglisttesting.data.remote.responses.API
import com.example.shoppinglisttesting.oher.Constants.BASE_URL
import com.example.shoppinglisttesting.oher.Constants.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class Appmodule {

    @Singleton
    @Provides
    fun provideShoppingItemDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context,
        ShoppingItemDataBase::class.java, DATABASE_NAME)
        .build()

    @Singleton
    @Provides
    fun provideDefaultShoppingRepository(
        dao: ShoppingDao,
        api: API,
    ) = DefaultShoppingRepository(dao,api) as ShoppingRepository

    @Singleton
    @Provides
    fun provideShoppingDao(
        dataBase: ShoppingItemDataBase
    ) = dataBase.shoppingDao()

    @Singleton
    @Provides
    fun provideAPI():API{
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build().create(API::class.java)
    }

}