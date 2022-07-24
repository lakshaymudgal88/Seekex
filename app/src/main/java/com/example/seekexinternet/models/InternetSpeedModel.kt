package com.example.seekexinternet.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "InternetSpeedModel")
data class InternetSpeedModel(
    val time: String = "",
    val speed: String = "0"
){
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
