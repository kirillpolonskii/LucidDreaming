package com.youngsophomore.luciddreaming

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.room.Room
import com.youngsophomore.luciddreaming.data.local.DreamDatabase
import com.youngsophomore.luciddreaming.data.repository.DreamRepository
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class LucidDreamingApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notifsChannel = NotificationChannel(
                    NOTIFS_CHANNEL_ID,
                    NOTIFS_CHANEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
                )
            val notifsManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notifsManager.createNotificationChannel(notifsChannel)
        }
    }

    companion object {
        val NOTIFS_CHANNEL_ID = "notifications_channel_id"
        val NOTIFS_CHANEL_NAME = "Слабые уведомления"
    }

}