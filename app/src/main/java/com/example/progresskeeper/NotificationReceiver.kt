package com.example.progresskeeper

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notification = Notification(context)
        notification.showWeeklyProgressNotification()
    }
} 