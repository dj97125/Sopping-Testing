package com.example.shoppinglisttesting.di

import android.content.Context
import androidx.room.Room
import com.example.shoppinglisttesting.data.local.ShoppingItemDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Named


@Module
@InstallIn(ApplicationComponent::class)
class TestAppModule {

    @Provides
    @Named("test_db")
    fun provideInMemoryDb(@ApplicationContext context: Context) =
        Room.inMemoryDatabaseBuilder(context,ShoppingItemDataBase::class.java)
            .allowMainThreadQueries()
            .build()
}