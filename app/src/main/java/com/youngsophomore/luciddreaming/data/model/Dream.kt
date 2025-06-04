package com.youngsophomore.luciddreaming.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "dreams")
data class Dream(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val content: String,
    val isFirstPerson: Boolean,
    val feelings: String, // format: <feeling1>|<feeling2>|...<feelingN>|
    val locations: String, // format: <location1>|<location2>|...<locationN>|
    val creationDateTime: LocalDateTime,
    val changeDateTime: LocalDateTime
)
