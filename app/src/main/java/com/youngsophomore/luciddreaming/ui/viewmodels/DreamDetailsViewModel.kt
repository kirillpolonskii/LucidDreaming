package com.youngsophomore.luciddreaming.ui.viewmodels

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import android.util.Log
import androidx.datastore.preferences.core.stringPreferencesKey
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

    var curDreamMoodsStr = ""
    val curDreamMoods = mutableListOf<String>()
    val curDreamLocations = mutableListOf<String>()
    val dreamMoodsIds = mutableListOf<Int>()
    val dreamLocationsIds = mutableListOf<Int>()
    var isNewMetaItemMood: Boolean? = null
    init {
        Log.d("Lifecycle", "DreamDetailsViewModel.init")
        //initMoods()
    }
    fun initMoodsAndLocations(ibtnMoodsId: Int, ibtnLocationsId: Int) {
        dreamMoodsIds.add(ibtnMoodsId)
        dreamLocationsIds.add(ibtnLocationsId)
    }

    fun addDreamMood(mood: String, moodId: Int){
        curDreamMoods.add(mood)
        dreamMoodsIds.add(if (dreamMoodsIds.size > 0) dreamMoodsIds.size - 1 else 0, moodId)
    }

    fun deleteDreamMood(mood: String, moodId: Int){
        curDreamMoods.remove(mood)
        dreamMoodsIds.remove(moodId)
    }

    fun addDreamLocation(location: String, locationId: Int){
        curDreamLocations.add(location)
        dreamLocationsIds.add(if (dreamLocationsIds.size > 0) dreamLocationsIds.size - 1 else 0, locationId)
    }

    fun deleteDreamLocation(location: String, locationId: Int){
        curDreamLocations.remove(location)
        dreamLocationsIds.remove(locationId)
    }

    fun addDream() = viewModelScope.launch {
        repository.addDream(Dream(
            title = "Title1",
            content = "Content of dream1",
            isFirstPerson = true,
            locations = "",
            feelings = "",
            creationDateTime = LocalDateTime.now(),
            changeDateTime = LocalDateTime.now()
        ))
    }
}