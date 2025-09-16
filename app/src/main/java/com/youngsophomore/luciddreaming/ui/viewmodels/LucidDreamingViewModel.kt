package com.youngsophomore.luciddreaming.ui.viewmodels

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youngsophomore.luciddreaming.utils.LDTheme
import com.youngsophomore.luciddreaming.utils.sha256
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class LucidDreamingViewModel @Inject constructor(
    private val dataStore: DataStore<Preferences>
)  : ViewModel() {
    val feelings = MutableLiveData<MutableList<String>>(mutableListOf())
    val locations = MutableLiveData<MutableList<String>>(mutableListOf())

    var ivThemesSelectedState = MutableLiveData<MutableList<Boolean>>()
    var selectedTheme = 0

    var notifsIsEnabled = false
    var notifsActiveHours = mutableListOf(0L, 0L)
    val notifsActiveHoursCalendarStart = MutableLiveData<Calendar>()
    val notifsActiveHoursCalendarEnd = MutableLiveData<Calendar>()
    var notifsFrequency = 1
    var notifsTimePoints = mutableListOf<Long>()
    var isPasswordEnabled: Boolean = false

    fun initTheme() = runBlocking {
        val keyTheme = intPreferencesKey("theme")
        val preferences = dataStore.data.first()
        selectedTheme = preferences[keyTheme] ?: 0
        ivThemesSelectedState.value = (BooleanArray(4){i -> if (i == selectedTheme) true else false}).toMutableList()

    }

    fun initFromPrefs() = viewModelScope.launch {
        
        val keyFeelings = stringPreferencesKey("feelings")
        val keyLocations = stringPreferencesKey("locations")

        val keyEnablePassword = booleanPreferencesKey("enable_password")
        val preferences = dataStore.data.first()
        
        val feelingsString = preferences[keyFeelings] ?: ""
        val locationsString = preferences[keyLocations] ?: ""

        isPasswordEnabled = preferences[keyEnablePassword] ?: false
        
        feelings.value?.addAll(feelingsString.split("|"))
        locations.value?.addAll(locationsString.split("|"))
        

    }

    fun appendFeeling(newFeeling: String){
        feelings.value?.add(newFeeling)
        val keyFeelings = stringPreferencesKey("feelings")
        viewModelScope.launch {
            dataStore.edit { prefs ->
                val curFeelings = prefs[keyFeelings] ?: ""
                
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
                
                prefs[keyFeelings] = feelings.value?.joinToString()!!
            }
        }
    }

    fun appendLocation(newLocation: String){
        locations.value?.add(newLocation)
        val keyLocations = stringPreferencesKey("locations")
        viewModelScope.launch {
            dataStore.edit { prefs ->
                val curLocations = prefs[keyLocations] ?: ""
                
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
                
                prefs[keyLocations] = locations.value?.joinToString()!!
            }
        }
    }

    fun setSelectedTheme(theme: LDTheme){
        selectedTheme = theme.ordinal
        ivThemesSelectedState.value = (BooleanArray(4){i -> if (i == theme.ordinal) true else false}).toMutableList()
        val keyTheme = intPreferencesKey("theme")
        viewModelScope.launch {
            dataStore.edit { prefs ->
                prefs[keyTheme] = theme.ordinal
            }
        }
    }

    fun initSettings() = viewModelScope.launch {
        val keyNotifsEnabled = booleanPreferencesKey("notifs_enabled")
        val keyNotifsActiveHours = stringPreferencesKey("notifs_active_hours")
        val keyNotifsFrequency = intPreferencesKey("notifs_frequency")
        val preferences = dataStore.data.first()

        notifsIsEnabled = preferences[keyNotifsEnabled] ?: false
        val notifsActiveHoursStrPref = preferences[keyNotifsActiveHours] ?: "00:00"
        notifsActiveHours =
            notifsActiveHoursStrPref
                .split(":")
                .map { it.toLong() }
                .toMutableList()
        notifsActiveHoursCalendarStart.value = Calendar.getInstance().apply {
            timeInMillis = notifsActiveHours[0]
        }
        notifsActiveHoursCalendarEnd.value = Calendar.getInstance().apply {
            timeInMillis = notifsActiveHours[1]
        }
        notifsFrequency = preferences[keyNotifsFrequency] ?: 1

    }

    fun setPassword(password: String) {
        val keyPassword = stringPreferencesKey("hash_password")
        val keyEnablePassword = booleanPreferencesKey("enable_password")
        viewModelScope.launch {
            dataStore.edit { prefs ->
                prefs[keyPassword] = password.sha256()
                prefs[keyEnablePassword] = true
                isPasswordEnabled = true
            }
        }

    }



    fun updateNotifsEnabled(isChecked: Boolean) {
        notifsIsEnabled = isChecked
        val keyNotifsEnabled = booleanPreferencesKey("notifs_enabled")
        viewModelScope.launch {
            dataStore.edit { prefs ->
                prefs[keyNotifsEnabled] = isChecked
            }
        }
    }

    fun updateActiveHours(hours: Int, minutes: Int, isStart: Boolean){
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hours)
        calendar.set(Calendar.MINUTE, minutes)
        
        if (isStart) {
            notifsActiveHours[0] = calendar.timeInMillis
            notifsActiveHoursCalendarStart.value = calendar
        }
        else {
            notifsActiveHours[1] = calendar.timeInMillis
            notifsActiveHoursCalendarEnd.value = calendar
        }
        val keyNotifsActiveHours = stringPreferencesKey("notifs_active_hours")
        viewModelScope.launch {
            dataStore.edit { prefs ->
                prefs[keyNotifsActiveHours] = "${notifsActiveHours[0]}:${notifsActiveHours[1]}"
            }
        }
        updateTimePoints()

    }

    fun updateFrequency(newFrequency: Int){
        notifsFrequency = newFrequency
        val keyNotifsFrequency = intPreferencesKey("notifs_frequency")
        viewModelScope.launch {
            dataStore.edit { prefs ->
                prefs[keyNotifsFrequency] = newFrequency
            }
        }
        updateTimePoints()
    }

    fun updateTimePoints(){
        notifsTimePoints.clear()
        val timeInterval = (notifsActiveHours[1] - notifsActiveHours[0]) / notifsFrequency
        for (i in 0..<notifsFrequency){
            notifsTimePoints.add(notifsActiveHours[0] + i * timeInterval)
            val cal = Calendar.getInstance()
            cal.timeInMillis = notifsActiveHours[0] + i * timeInterval
        }
    }

    suspend fun checkPassword(password: String): Boolean{
        val keyPassword = stringPreferencesKey("hash_password")
        //val keyEnablePassword = booleanPreferencesKey("enable_password")
        val preferences = dataStore.data.first()
        //val enablePassword = preferences[keyEnablePassword] ?: false
        val hashPassword = preferences[keyPassword] ?: ""
        return hashPassword == password.sha256()
    }

    fun disablePassword(){
        val keyEnablePassword = booleanPreferencesKey("enable_password")
        viewModelScope.launch {
            dataStore.edit { prefs ->
                prefs[keyEnablePassword] = false
                isPasswordEnabled = false
            }
        }
    }
}