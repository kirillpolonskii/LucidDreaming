package com.youngsophomore.luciddreaming.utils

import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import java.security.MessageDigest
import kotlin.io.encoding.Base64

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

fun String.sha256(): String {
    val messageDigest = MessageDigest.getInstance("SHA-256")
    val byteArray = messageDigest.digest(this.toByteArray())
    val hashedValue = StringBuffer()
    for (byte in byteArray) {
        hashedValue.append(Integer.toHexString(0xFF and byte.toInt()))
    }
    return hashedValue.toString()
}