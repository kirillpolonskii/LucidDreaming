package com.youngsophomore.luciddreaming.data.repository

import com.youngsophomore.luciddreaming.data.local.DreamDao
import com.youngsophomore.luciddreaming.data.model.Dream
import kotlinx.coroutines.flow.Flow

class DreamRepository(private val dreamDao: DreamDao) {
    suspend fun addDream(dream: Dream){
        dreamDao.insert(dream)
    }

    suspend fun getAllDreams() : Flow<List<Dream>>{
        return dreamDao.getAllDreams()
    }
}