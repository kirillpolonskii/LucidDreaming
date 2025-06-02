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
    val feelings = MutableLiveData<MutableList<String>>(mutableListOf())
    val locations = MutableLiveData<MutableList<String>>(mutableListOf())

    fun initFeelingsAndLocations() = viewModelScope.launch {
        Log.d("Preferences", "LucidDreamingViewModel.initFeelingsAndLocations")
        val keyFeelings = stringPreferencesKey("feelings")
        val keyLocations = stringPreferencesKey("locations")
        val preferences = dataStore.data.first()
        Log.d("Preferences", " preferences = $preferences")
        val feelingsString = preferences[keyFeelings] ?: ""
        val locationsString = preferences[keyLocations] ?: ""
        Log.d("Preferences", " feelingsString = $feelingsString")
        feelings.value?.addAll(feelingsString.split("|"))
        locations.value?.addAll(locationsString.split("|"))
        Log.d("Preferences", " feelings = $feelings")
    }

    fun appendFeeling(newFeeling: String){
        Log.d("Preferences", "LucidDreamingViewModel.appendFeeling, $newFeeling")
        feelings.value?.add(newFeeling)
        val keyFeelings = stringPreferencesKey("feelings")
        viewModelScope.launch {
            dataStore.edit { prefs ->
                val curFeelings = prefs[keyFeelings] ?: ""
                Log.d("Preferences", " $curFeelings")
                if (curFeelings.isEmpty())
                    prefs[keyFeelings] = "$curFeelings$newFeeling"
                else
                    prefs[keyFeelings] = "$curFeelings|$newFeeling"
            }
        }
    }

    fun deleteFeeling(feeling: String){
        feelings.value?.remove(feeling)
        val keyFeelings = stringPreferencesKey("feelings")
        viewModelScope.launch {
            dataStore.edit { prefs ->
                Log.d("Preferences", " " + feelings.value?.joinToString())
                prefs[keyFeelings] = feelings.value?.joinToString()!!
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