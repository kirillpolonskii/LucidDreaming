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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class DreamDetailsViewModel @Inject constructor(private val repository: DreamRepository) : ViewModel() {
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