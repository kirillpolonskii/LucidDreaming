package com.youngsophomore.luciddreaming.ui.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.random.Random

class MainMenuViewModel : ViewModel() {
    private val predefButtonsBiases = mutableListOf(
        mutableListOf(0.1f, 0.1f, 0.5f, 0.5f, 0.9f, 0.9f),
        mutableListOf(0.5f, 0.1f, 0.5f, 0.5f, 0.5f, 0.9f),
        mutableListOf(0.1f, 0.9f, 0.5f, 0.9f, 0.9f, 0.9f)
    )
    internal var curButtonsBiases = mutableListOf<Float>()

    init {
        Log.d("Debug_app", "in init{} in MainMenuViewModel")
        setNewBiases()
    }

    internal fun setNewBiases(){
        curButtonsBiases = predefButtonsBiases[Random.nextInt(0, predefButtonsBiases.size)]
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("Debug_app", "in onCleared() in MainMenuViewModel")
    }


}