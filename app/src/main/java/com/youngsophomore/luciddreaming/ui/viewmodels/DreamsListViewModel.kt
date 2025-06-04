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
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class DreamsListViewModel @Inject constructor(val repository: DreamRepository) : ViewModel() {

    val allDreams: LiveData<List<Dream>> = repository.getAllDreams()
    var filteredDreams = listOf<Dream>()
    var isDreamFirstPerson: Boolean? = null
    private val filterFeelings = mutableListOf<String>()
    private val filterLocations = mutableListOf<String>()
    val filterFeelingsIds = mutableListOf<Int>()
    val filterLocationsIds = mutableListOf<Int>()
    var isNewFilterItemFeeling: Boolean? = null
    val filterKeywords = mutableListOf<String>()
    val filterKeywordsIds = mutableListOf<Int>()
    var dateRangeCreated: Pair<LocalDateTime, LocalDateTime>? = null
    var dateRangeEdited: Pair<LocalDateTime, LocalDateTime>? = null

    init {
        Log.d("Debug", "DreamsListViewModel.init")
        Log.d("Debug", " allDreams = ${allDreams.value}")

        //filteredDreams.value = allDreams.value
    }

    fun initFeelingsAndLocations(ibtnFeelingsId: Int, ibtnLocationsId: Int) {
        filterFeelingsIds.add(ibtnFeelingsId)
        filterLocationsIds.add(ibtnLocationsId)
    }

    fun addFilterFeeling(feeling: String, feelingId: Int){
        filterFeelings.add(feeling)
        filterFeelingsIds.add(if (filterFeelingsIds.size > 0) filterFeelingsIds.size - 1 else 0, feelingId)
    }

    fun deleteFilterFeeling(feeling: String, feelingId: Int){
        filterFeelings.remove(feeling)
        filterFeelingsIds.remove(feelingId)
    }

    fun addFilterLocation(location: String, locationId: Int){
        filterLocations.add(location)
        filterLocationsIds.add(if (filterLocationsIds.size > 0) filterLocationsIds.size - 1 else 0, locationId)
    }

    fun deleteFilterLocation(location: String, locationId: Int){
        filterLocations.remove(location)
        filterLocationsIds.remove(locationId)
    }

    fun addFilterKeyword(keyword: String, keywordId: Int){
        filterKeywords.add(keyword)
        filterKeywordsIds.add(keywordId)
    }
    fun deleteFilterKeyword(keyword: String, keywordId: Int){
        filterKeywords.remove(keyword)
        filterKeywordsIds.remove(keywordId)
    }

    fun applyFilters(){

        filteredDreams = allDreams.value ?: listOf()
        filteredDreams = filteredDreams
            .filter {
                var ans = filterKeywords.isEmpty()
                for (keyword in filterKeywords)
                    if (it.title.contains(keyword) || it.content.contains(keyword)) {
                        ans = true
                        break
                    }
                ans
            }
            .also {
                Log.d("Debug", " aft keyword filter, " + it.joinToString())
            }
            .filter {
                if (isDreamFirstPerson != null) it.isFirstPerson == isDreamFirstPerson
                else true
            }
            .also {
                Log.d("Debug", " aft pov filter, " + it.joinToString())
            }
            .filter {
                var ans = filterFeelings.isEmpty()
                for (feeling in filterFeelings)
                    if (it.feelings.contains(feeling)) {
                        ans = true
                        break
                    }
                ans
            }
            .also {
                Log.d("Debug", " aft feeling filter, " + it.joinToString())
            }
            .filter {
                var ans = filterLocations.isEmpty()
                for (location in filterLocations)
                    if (it.locations.contains(location)) {
                        ans = true
                        break
                    }
                ans
            }
            .also {
                Log.d("Debug", " aft locs filter, " + it.joinToString())
            }
            .filter {
                if (dateRangeCreated != null)
                    it.creationDateTime.isAfter(dateRangeCreated!!.first)
                            && it.creationDateTime.isBefore(dateRangeCreated!!.second)
                else true
            }
            .also {
                Log.d("Debug", " aft created filter, " + it.joinToString())
            }
            .filter {
                if (dateRangeEdited != null)
                    it.changeDateTime.isAfter(dateRangeEdited!!.first)
                            && it.changeDateTime.isBefore(dateRangeEdited!!.second)
                else true
            }
            .also {
                Log.d("Debug", " aft edited filter, " + it.joinToString())
            }


    }


}