package com.youngsophomore.luciddreaming.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.youngsophomore.luciddreaming.data.model.Dream
import kotlinx.coroutines.flow.Flow

@Dao
interface DreamDao {
    @Insert
    suspend fun insert(dream: Dream): Long

    @Update
    suspend fun update(dream: Dream)

    @Delete
    suspend fun delete(dream: Dream)

    @Query("SELECT * FROM dreams ORDER BY id")
    fun getAllDreams(): LiveData<List<Dream>>
}