package com.example.seekexinternet.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.seekexinternet.models.InternetSpeedModel

@Database(entities = [InternetSpeedModel::class], version = 1)
abstract class RoomInstance: RoomDatabase() {

    abstract fun getDao(): RoomDao
}