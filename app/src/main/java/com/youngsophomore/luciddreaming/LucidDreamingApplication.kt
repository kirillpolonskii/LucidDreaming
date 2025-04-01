package com.youngsophomore.luciddreaming

import android.app.Application
import androidx.room.Room
import com.youngsophomore.luciddreaming.data.local.DreamDatabase
import com.youngsophomore.luciddreaming.data.repository.DreamRepository

class LucidDreamingApplication : Application() {
    val database = Room.databaseBuilder(
        this,
        DreamDatabase::class.java,
        "dream_database"
    ).build()
    val repository = DreamRepository(
        database.dreamDao()
    )
}