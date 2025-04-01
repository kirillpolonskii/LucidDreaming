package com.youngsophomore.luciddreaming.di

import android.content.Context
import androidx.room.Room
import com.youngsophomore.luciddreaming.data.local.DreamDao
import com.youngsophomore.luciddreaming.data.local.DreamDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object RoomModule {
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): DreamDatabase{
        return Room.databaseBuilder(
            context,
            DreamDatabase::class.java,
            "dream_database"
        ).build()
    }

    @Provides
    fun provideDreamDao(dreamDatabase: DreamDatabase): DreamDao {
        return dreamDatabase.dreamDao()
    }
}