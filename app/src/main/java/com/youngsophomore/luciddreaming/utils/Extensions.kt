package com.youngsophomore.luciddreaming.utils

import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet

fun ConstraintSet.setBiases(
    cnstLayt: ConstraintLayout,
    idToBiases: Map<Int, Pair<Float, Float>>
){
    this.clone(cnstLayt)
    idToBiases.forEach { id, biases ->
        this.apply {
            setHorizontalBias(id, biases.first)
            setVerticalBias(id, biases.second)
        }
    }
    this.applyTo(cnstLayt)
}