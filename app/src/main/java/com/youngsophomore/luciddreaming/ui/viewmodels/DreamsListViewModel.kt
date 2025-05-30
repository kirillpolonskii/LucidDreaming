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
    private val searchFeelings = mutableListOf<String>()
    private val searchLocations = mutableListOf<String>()
    val searchFeelingsIds = mutableListOf<Int>()
    val searchLocationsIds = mutableListOf<Int>()
    var isNewSearchItemFeeling: Boolean? = null
    var isDreamFirstPerson = true
    init {
        Log.d("Debug", "DreamsListViewModel.init")
    }

    fun initFeelingsAndLocations(ibtnFeelingsId: Int, ibtnLocationsId: Int) {
        searchFeelingsIds.add(ibtnFeelingsId)
        searchLocationsIds.add(ibtnLocationsId)
    }

    fun addSearchFeeling(feeling: String, feelingId: Int){
        searchFeelings.add(feeling)
        searchFeelingsIds.add(if (searchFeelingsIds.size > 0) searchFeelingsIds.size - 1 else 0, feelingId)
    }

    fun deleteSearchFeeling(feeling: String, feelingId: Int){
        searchFeelings.remove(feeling)
        searchFeelingsIds.remove(feelingId)
    }

    fun addSearchLocation(location: String, locationId: Int){
        searchLocations.add(location)
        searchLocationsIds.add(if (searchLocationsIds.size > 0) searchLocationsIds.size - 1 else 0, locationId)
    }

    fun deleteSearchLocation(location: String, locationId: Int){
        searchLocations.remove(location)
        searchLocationsIds.remove(locationId)
    }



}