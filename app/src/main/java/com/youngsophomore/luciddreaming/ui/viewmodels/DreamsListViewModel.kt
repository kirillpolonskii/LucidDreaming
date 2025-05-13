package com.youngsophomore.luciddreaming.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youngsophomore.luciddreaming.data.model.Dream
import com.youngsophomore.luciddreaming.data.repository.DreamRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DreamsListViewModel @Inject constructor(repository: DreamRepository) : ViewModel() {

    val allDreams: LiveData<List<Dream>>

    init {
        allDreams = repository.getAllDreams()
    }

    //fun getAllDreams() = //viewModelScope.launch {
     //   allDreams
    //}




}