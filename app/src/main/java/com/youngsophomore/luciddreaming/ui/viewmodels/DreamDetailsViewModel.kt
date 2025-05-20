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
    lateinit var moods: List<String>
    init {
        Log.d("Lifecycle", "DreamDetailsViewModel.init")
        //initMoods()
    }
    fun initMoods() = viewModelScope.launch{
        Log.d("Preferences", "DreamDetailsViewModel.getMoods")
        val keyMoods = stringPreferencesKey("moods")
        val preferences = dataStore.data.first()
        Log.d("Preferences", " preferences = $preferences")
        val moodsString = preferences[keyMoods] ?: ""
        Log.d("Preferences", " moodsString = $moodsString")
        moods = moodsString.split("|")
        Log.d("Preferences", " moods = $moods")
    }

    fun updateMoods(newMood: String){
        Log.d("Preferences", "DreamDetailsViewModel.updateMoods, $newMood")
        val keyMoods = stringPreferencesKey("moods")
        viewModelScope.launch {
            dataStore.edit { prefs ->
                val curMoods = prefs[keyMoods] ?: ""
                prefs[keyMoods] = "$curMoods|$newMood"
            }
        }
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