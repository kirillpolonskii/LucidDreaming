package com.youngsophomore.luciddreaming

import android.app.Application
import androidx.room.Room
import com.youngsophomore.luciddreaming.data.local.DreamDatabase
import com.youngsophomore.luciddreaming.data.repository.DreamRepository
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class LucidDreamingApplication : Application() {


}