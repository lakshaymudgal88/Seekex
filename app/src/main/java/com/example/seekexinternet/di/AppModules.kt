package com.example.seekexinternet.di

import android.content.Context
import androidx.room.Room
import com.example.seekexinternet.constants.Constants.DB_NAME
import com.example.seekexinternet.db.RoomInstance
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModules {

    @Provides
    @Singleton
    fun provideRoomDbInstance(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, RoomInstance::class.java, DB_NAME)
            .build()

    @Provides
    @Singleton
    fun provideRoomDao(roomInstance: RoomInstance) =
        roomInstance.getDao()


}