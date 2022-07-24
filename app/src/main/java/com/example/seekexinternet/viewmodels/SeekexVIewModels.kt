package com.example.seekexinternet.viewmodels

import androidx.lifecycle.ViewModel
import com.example.seekexinternet.repository.SeekexRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SeekexVIewModels @Inject constructor(private val seekexRepository: SeekexRepository) :
    ViewModel() {

    fun getSpeedList() =
        seekexRepository.getSpeedList()
}