package com.youngsophomore.luciddreaming.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import com.youngsophomore.luciddreaming.LucidDreamingApplication
import com.youngsophomore.luciddreaming.data.model.Dream
import com.youngsophomore.luciddreaming.data.repository.DreamRepository
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class DreamDetailsViewModel(private val repository: DreamRepository) : ViewModel() {
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

    companion object {

        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                // Get the Application object from extras
                val application = checkNotNull(extras[APPLICATION_KEY])
                // Create a SavedStateHandle for this ViewModel from extras
                //val savedStateHandle = extras.createSavedStateHandle()

                return DreamDetailsViewModel(
                    (application as LucidDreamingApplication).repository
                    //savedStateHandle
                ) as T
            }
        }
    }
}