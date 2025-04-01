package com.youngsophomore.luciddreaming.data.local

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.youngsophomore.luciddreaming.data.model.Dream
import com.youngsophomore.luciddreaming.utils.Converters

@Database(entities = [Dream::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class DreamDatabase : RoomDatabase() {
    abstract fun dreamDao(): DreamDao
    companion object {
        @Volatile
        private var INSTANCE: DreamDatabase? = null
        fun getDatabase(context: Context): DreamDatabase {
            Log.d("Debug tag", "in getDatabase()")
            return INSTANCE ?: synchronized(this) {
                Log.d("Debug tag", "in synchronized()")
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DreamDatabase::class.java,
                    "dream_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}