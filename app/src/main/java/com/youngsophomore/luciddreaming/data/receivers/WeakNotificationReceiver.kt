package com.youngsophomore.luciddreaming.data.receivers

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.youngsophomore.luciddreaming.LucidDreamingApplication

class WeakNotificationReceiver : BroadcastReceiver() {
    private var notifManager: NotificationManagerCompat? = null
    override fun onReceive(context: Context?, intent: Intent?) {

        val weakNotif = context?.let { context ->
            // тег канала потом нужно переместить в LucidDreamingApplication,
            // где будет создаваться канал для слабых уведомлений
            NotificationCompat.Builder(context, LucidDreamingApplication.WEAK_NOTIFS_CHANNEL_ID)
                .setContentTitle("Проверка реальности")
                .setContentText("Выполните проверку окружения на реальность")
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .build()
        }
        notifManager = context?.let { NotificationManagerCompat.from(it) }
        if (ActivityCompat.checkSelfPermission(
            context!!, Manifest.permission.POST_NOTIFICATIONS
        ) != PackageManager.PERMISSION_GRANTED){
            return
        }
        notifManager?.notify(1, weakNotif!!)
    }

}