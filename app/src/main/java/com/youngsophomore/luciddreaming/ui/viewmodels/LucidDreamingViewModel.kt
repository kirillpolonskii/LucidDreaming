package com.youngsophomore.luciddreaming.ui.viewmodels

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youngsophomore.luciddreaming.data.repository.DreamRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LucidDreamingViewModel @Inject constructor(
    private val dataStore: DataStore<Preferences>
)  : ViewModel() {
    val moods = MutableLiveData<MutableList<String>>(mutableListOf())
    val locations = MutableLiveData<MutableList<String>>(mutableListOf())

    fun initMoodsAndLocations() = viewModelScope.launch {
        Log.d("Preferences", "LucidDreamingViewModel.initMoodsAndLocations")
        val keyMoods = stringPreferencesKey("moods")
        val keyLocations = stringPreferencesKey("locations")
        val preferences = dataStore.data.first()
        Log.d("Preferences", " preferences = $preferences")
        val moodsString = preferences[keyMoods] ?: ""
        val locationsString = preferences[keyLocations] ?: ""
        Log.d("Preferences", " moodsString = $moodsString")
        moods.value?.addAll(moodsString.split("|"))
        locations.value?.addAll(locationsString.split("|"))
        Log.d("Preferences", " moods = $moods")
    }

    fun appendMood(newMood: String){
        Log.d("Preferences", "LucidDreamingViewModel.appendMood, $newMood")
        moods.value?.add(newMood)
        val keyMoods = stringPreferencesKey("moods")
        viewModelScope.launch {
            dataStore.edit { prefs ->
                val curMoods = prefs[keyMoods] ?: ""
                Log.d("Preferences", " $curMoods")
                if (curMoods.isEmpty())
                    prefs[keyMoods] = "$curMoods$newMood"
                else
                    prefs[keyMoods] = "$curMoods|$newMood"
            }
        }
    }

    fun deleteMood(mood: String){
        moods.value?.remove(mood)
        val keyMoods = stringPreferencesKey("moods")
        viewModelScope.launch {
            dataStore.edit { prefs ->
                Log.d("Preferences", " " + moods.value?.joinToString())
                prefs[keyMoods] = moods.value?.joinToString()!!
            }
        }
    }

    fun appendLocation(newLocation: String){
        Log.d("Preferences", "LucidDreamingViewModel.appendLocation, $newLocation")
        locations.value?.add(newLocation)
        val keyLocations = stringPreferencesKey("locations")
        viewModelScope.launch {
            dataStore.edit { prefs ->
                val curLocations = prefs[keyLocations] ?: ""
                Log.d("Preferences", " $curLocations")
                if (curLocations.isEmpty())
                    prefs[keyLocations] = "$curLocations$newLocation"
                else
                    prefs[keyLocations] = "$curLocations|$newLocation"
            }
        }
    }

    fun deleteLocation(location: String){
        locations.value?.remove(location)
        val keyLocations = stringPreferencesKey("locations")
        viewModelScope.launch {
            dataStore.edit { prefs ->
                Log.d("Preferences", " " + locations.value?.joinToString())
                prefs[keyLocations] = locations.value?.joinToString()!!
            }
        }
    }
}