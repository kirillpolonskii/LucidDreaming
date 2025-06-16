package com.youngsophomore.luciddreaming.ui.viewmodels

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : ViewModel() {

    val weakNotifsIsEnabled = MutableLiveData(false)
    var weakNotifsActiveHours = mutableListOf(0L, 0L)
    val weakNotifsActiveHoursCalendarStart = MutableLiveData<Calendar>()
    val weakNotifsActiveHoursCalendarEnd = MutableLiveData<Calendar>()
    var weakNotifsFrequency = 1
    var weakNotifsMessage = ""
    var weakNotifsTimePoints = mutableListOf<Long>()

    fun initSettings() = viewModelScope.launch {

        val keyWeakNotifsEnabled = booleanPreferencesKey("weak_notifs_enabled")
        val keyWeakNotifsActiveHours = stringPreferencesKey("weak_notifs_active_hours")
        val keyWeakNotifsFrequency = intPreferencesKey("weak_notifs_frequency")
        val keyWeakNotifsMessage = stringPreferencesKey("weak_notifs_message")
        val preferences = dataStore.data.first()

        weakNotifsIsEnabled.value = preferences[keyWeakNotifsEnabled] ?: false
        val weakNotifsActiveHoursStrPref = preferences[keyWeakNotifsActiveHours] ?: "0-0"
        weakNotifsActiveHours =
            weakNotifsActiveHoursStrPref
                .split("-")
                .map { it.toLong() }
                .toMutableList()
        weakNotifsActiveHoursCalendarStart.value = Calendar.getInstance().apply {
            timeInMillis = weakNotifsActiveHours[0]
        }
        weakNotifsActiveHoursCalendarEnd.value = Calendar.getInstance().apply {
            timeInMillis = weakNotifsActiveHours[1]
        }
        weakNotifsFrequency = preferences[keyWeakNotifsFrequency] ?: 1
        weakNotifsMessage = preferences[keyWeakNotifsMessage] ?: "СООБЩЕНИЕ СЛАБОГО УВЕДОМЛЕНИЯ"

    }

    fun updateActiveHours(hours: Int, minutes: Int, isStart: Boolean){
        Log.d("Debug", "SettingsViewModel.updateActiveHours()")
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hours)
        calendar.set(Calendar.MINUTE, minutes)
        Log.d("Debug", " $calendar")
        if (isStart) {
            weakNotifsActiveHours[0] = calendar.timeInMillis
            weakNotifsActiveHoursCalendarStart.value = calendar
        }
        else {
            weakNotifsActiveHours[1] = calendar.timeInMillis
            weakNotifsActiveHoursCalendarEnd.value = calendar
        }
        val keyWeakNotifsActiveHours = stringPreferencesKey("weak_notifs_active_hours")
        viewModelScope.launch {
            dataStore.edit { prefs ->
                //Log.d("Preferences", " " + locations.value?.joinToString())
                prefs[keyWeakNotifsActiveHours] = "${weakNotifsActiveHours[0]}-${weakNotifsActiveHours[1]}"
            }
        }
        updateTimePoints()

    }

    fun updateFrequency(newFrequency: Int){
        weakNotifsFrequency = newFrequency
        val keyWeakNotifsFrequency = intPreferencesKey("weak_notifs_frequency")
        viewModelScope.launch {
            dataStore.edit { prefs ->
                //Log.d("Preferences", " " + locations.value?.joinToString())
                prefs[keyWeakNotifsFrequency] = newFrequency
            }
        }
        updateTimePoints()
    }

    fun updateTimePoints(){
        Log.d("Debug", "SettingsViewModel.updateTimePoints()")
        weakNotifsTimePoints.clear()
        val timeInterval = (weakNotifsActiveHours[1] - weakNotifsActiveHours[0]) / weakNotifsFrequency
        for (i in 0..<weakNotifsFrequency){
            weakNotifsTimePoints.add(weakNotifsActiveHours[0] + i * timeInterval)
            val cal = Calendar.getInstance()
            cal.timeInMillis = weakNotifsActiveHours[0] + i * timeInterval
            Log.d("Debug", " cur time point for alarm = ")
            Log.d("Debug", " ${cal.get(Calendar.DATE)}, ${cal.get(Calendar.HOUR_OF_DAY)}:${cal.get(Calendar.MINUTE)}:${cal.get(Calendar.SECOND)}")
        }
    }


}