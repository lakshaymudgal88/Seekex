package com.example.seekexinternet.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.seekexinternet.models.InternetSpeedModel

@Dao
interface RoomDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInternetSpeed(internetSpeedModel: InternetSpeedModel)

    @Query("SELECT * FROM InternetSpeedModel ORDER BY ID DESC")
    fun getSpeedList(): LiveData<List<InternetSpeedModel>>

    @Query("DELETE FROM InternetSpeedModel")
    suspend fun deleteData()
}