package com.youngsophomore.luciddreaming.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.youngsophomore.luciddreaming.data.model.Dream
import com.youngsophomore.luciddreaming.data.repository.DreamRepository
import com.youngsophomore.luciddreaming.ui.fragments.DreamDetailsFragmentArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class DreamDetailsViewModel @Inject constructor(
    private val repository: DreamRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val navArgs: DreamDetailsFragmentArgs = DreamDetailsFragmentArgs.fromSavedStateHandle(savedStateHandle)

    var dream = Dream(
        title = "",
        content = "",
        isFirstPerson = true,
        locations = "",
        feelings = "",
        creationDateTime = LocalDateTime.now(),
        //creationDateTime = dreamCreationDateTime,
        changeDateTime = LocalDateTime.now()
        //changeDateTime = dreamChangeDateTime
    )
    var dreamById: Flow<Dream>? = null
    lateinit var dreamListener: DreamArgsListener
    private var dreamId = 0
    private val dreamFeelings = mutableListOf<String>()
    private val dreamLocations = mutableListOf<String>()
    val dreamFeelingsIds = mutableListOf<Int>()
    val dreamLocationsIds = mutableListOf<Int>()
    var isNewMetaItemFeeling: Boolean? = null
    val isDreamEditable = MutableLiveData(true)
    private var isDreamInDB = false
    init {
        if (navArgs.dreamId != -1) {
            viewModelScope.launch {
                dreamById = repository.getDream(navArgs.dreamId)
                dream = dreamById!!.first()
                
                dreamListener.onDreamCollected(dream)
                isDreamInDB = true
                isDreamEditable.value = false
            }
        }
        
    }
    fun initFeelingsAndLocations(ibtnFeelingsId: Int, ibtnLocationsId: Int) {
        dreamFeelingsIds.add(ibtnFeelingsId)
        dreamLocationsIds.add(ibtnLocationsId)
        
    }

    fun addDreamFeeling(feeling: String, feelingId: Int){
        dreamFeelings.add(feeling)
        dreamFeelingsIds.add(if (dreamFeelingsIds.size > 0) dreamFeelingsIds.size - 1 else 0, feelingId)
    }

    fun deleteDreamFeeling(feeling: String, feelingId: Int){
        dreamFeelings.remove(feeling)
        dreamFeelingsIds.remove(feelingId)
    }

    fun addDreamLocation(location: String, locationId: Int){
        dreamLocations.add(location)
        dreamLocationsIds.add(if (dreamLocationsIds.size > 0) dreamLocationsIds.size - 1 else 0, locationId)
    }

    fun deleteDreamLocation(location: String, locationId: Int){
        dreamLocations.remove(location)
        dreamLocationsIds.remove(locationId)
    }

    fun addUpdateDream(
        title: String,
        content: String
    ) = viewModelScope.launch {
        if (!isDreamInDB) {
            dream = dream.copy(
                title = title,
                content = content,
                locations = dreamLocations.joinToString("|"),
                feelings = dreamFeelings.joinToString("|"),
                creationDateTime = LocalDateTime.now(),
                changeDateTime = LocalDateTime.now()
            )
            dreamId = repository.addDream(dream).toInt()
            isDreamInDB = true
            dream = dream.copy(
                id = dreamId
            )
        }
        else {
            dream = dream.copy(
                title = title,
                content = content,
                //isFirstPerson = isDreamFirstPerson,
                locations = dreamLocations.joinToString("|"),
                feelings = dreamFeelings.joinToString("|"),
                changeDateTime = LocalDateTime.now()
            )
            repository.addDream(dream)
        }

    }

    interface DreamArgsListener {
        fun onDreamCollected(dream: Dream)
    }
}