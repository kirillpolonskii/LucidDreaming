package com.youngsophomore.luciddreaming.data.repository

import androidx.lifecycle.LiveData
import com.youngsophomore.luciddreaming.data.local.DreamDao
import com.youngsophomore.luciddreaming.data.model.Dream
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DreamRepository @Inject constructor(private val dreamDao: DreamDao) {
    suspend fun addDream(dream: Dream): Long{
        return dreamDao.insert(dream)
    }

    suspend fun updateDream(dream: Dream) {
        dreamDao.update(dream)
    }

    fun getAllDreams() : LiveData<List<Dream>> {
        return dreamDao.getAllDreams()
    }
}