package com.youngsophomore.luciddreaming.ui.viewmodels

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import android.util.Log
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import com.youngsophomore.luciddreaming.LucidDreamingApplication
import com.youngsophomore.luciddreaming.data.model.Dream
import com.youngsophomore.luciddreaming.data.repository.DreamRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class DreamDetailsViewModel @Inject constructor(
    private val repository: DreamRepository,
    private val dataStore: DataStore<Preferences>
) : ViewModel() {

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
    private var dreamId = 0
    private val dreamFeelings = mutableListOf<String>()
    private val dreamLocations = mutableListOf<String>()
    val dreamFeelingsIds = mutableListOf<Int>()
    val dreamLocationsIds = mutableListOf<Int>()
    var isNewMetaItemFeeling: Boolean? = null
    val isDreamEditable = MutableLiveData<Boolean>(true)
    private var isDreamInDB = false
    var isDreamFirstPerson = true
    init {
        Log.d("Lifecycle", "DreamDetailsViewModel.init")
        //initFeelings()
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
        Log.d("Data", "DreamDetailsViewModel.addUpdateDream")
        Log.d("Data", " id = ${dream.id}")
        Log.d("Data", " " + dreamFeelings.joinToString("|"))
        Log.d("Data", " " + dreamLocations.joinToString("|"))
        Log.d("Data", " $isDreamInDB")
        if (!isDreamInDB) {
            dream = dream.copy(
                title = title,
                content = content,
                isFirstPerson = isDreamFirstPerson,
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
                isFirstPerson = isDreamFirstPerson,
                locations = dreamLocations.joinToString("|"),
                feelings = dreamFeelings.joinToString("|"),
                changeDateTime = LocalDateTime.now()
            )
            repository.addDream(dream)
        }
        /*if (!isDreamInDB) {
            dream = dream.copy(
                title = title,
                content = content,
                isFirstPerson = isDreamFirstPerson,
                locations = dreamLocations.joinToString("|"),
                feelings = dreamFeelings.joinToString("|"),
                creationDateTime = LocalDateTime.now(),
                changeDateTime = LocalDateTime.now()
            )
            repository.addDream(dream)
            isDreamInDB = true
        }
        else {
            dream = dream.copy(
                title = title,
                content = content,
                isFirstPerson = isDreamFirstPerson,
                locations = dreamLocations.joinToString("|"),
                feelings = dreamFeelings.joinToString("|"),
                changeDateTime = LocalDateTime.now()
            )
            repository.updateDream(dream)
        }*/
    }


}