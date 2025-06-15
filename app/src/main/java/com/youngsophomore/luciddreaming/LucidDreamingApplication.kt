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
            val weakNotifsChannel = NotificationChannel(
                    WEAK_NOTIFS_CHANNEL_ID,
                    WEAK_NOTIFS_CHANEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
                )
            val notifsManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notifsManager.createNotificationChannel(weakNotifsChannel)
        }
    }

    companion object {
        val WEAK_NOTIFS_CHANNEL_ID = "weak_notifications_channel_id"
        val WEAK_NOTIFS_CHANEL_NAME = "Слабые уведомления"
    }

}