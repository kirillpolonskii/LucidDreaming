package com.youngsophomore.luciddreaming.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlin.random.Random

class MainMenuViewModel : ViewModel() {
    private val predefButtonsBiases = mutableListOf(
        mutableListOf(0.1f to 0.1f, 0.5f to 0.5f, 0.9f to 0.9f),
        mutableListOf(0.5f to 0.1f, 0.5f to 0.5f, 0.5f to 0.9f),
        mutableListOf(0.1f to 0.9f, 0.5f to 0.9f, 0.9f to 0.9f)
    )
    internal var curButtonsBiases = mutableListOf<Pair<Float, Float>>()

    init {
        setNewBiases()
    }

    internal fun setNewBiases(){
        curButtonsBiases = predefButtonsBiases[Random.nextInt(0, predefButtonsBiases.size)]
    }

}