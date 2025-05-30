package com.youngsophomore.luciddreaming.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.youngsophomore.luciddreaming.data.model.Dream
import com.youngsophomore.luciddreaming.data.repository.DreamRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import hilt_aggregated_deps._dagger_hilt_android_internal_managers_HiltWrapper_ActivityRetainedComponentManager_ActivityRetainedLifecycleEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DreamsListViewModel @Inject constructor(val repository: DreamRepository) : ViewModel() {

    val allDreams: LiveData<List<Dream>> = repository.getAllDreams()
    
    init {
        Log.d("Debug", "DreamsListViewModel.init")
        //allDreams = repository.getAllDreams().asLiveData()

        //Log.d("Debug", " allDreams.size = ${allDreams.value?.size}")
    }

    fun fetchAllDreams(): LiveData<List<Dream>>{
        return repository.getAllDreams()
    }



}