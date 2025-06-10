package com.youngsophomore.luciddreaming.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.youngsophomore.luciddreaming.data.local.DreamDao
import com.youngsophomore.luciddreaming.data.model.Dream
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class DreamRepository @Inject constructor(private val dreamDao: DreamDao) {
    suspend fun addDream(dream: Dream): Long {
        return dreamDao.insert(dream)
    }

    suspend fun getDream(id: Int): Flow<Dream> {
        val dream = dreamDao.getDream(id)
        Log.d("Debug", "repository, dream = ${dream.first()}")
        return dream
    }

    /*suspend fun updateDream(dream: Dream) {
        dreamDao.update(dream)
    }*/

    fun getAllDreams() : LiveData<List<Dream>> {
        val dreams = dreamDao.getAllDreams()
        Log.d("Debug", "repository, dreams.size = ${dreams.value?.size}")
        return dreams
    }
}