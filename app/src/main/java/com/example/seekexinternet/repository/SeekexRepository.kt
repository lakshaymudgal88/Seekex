package com.example.seekexinternet.repository

import com.example.seekexinternet.db.RoomDao
import com.example.seekexinternet.models.InternetSpeedModel
import javax.inject.Inject

class SeekexRepository @Inject constructor(private val roomDao: RoomDao) {

    suspend fun insertSpeed(internetSpeedModel: InternetSpeedModel) =
        roomDao.insertInternetSpeed(internetSpeedModel)

    fun getSpeedList() =
        roomDao.getSpeedList()

    suspend fun deleteData() =
        roomDao.deleteData()

}