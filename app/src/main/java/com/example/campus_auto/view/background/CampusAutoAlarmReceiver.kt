package com.example.campus_auto.view.background

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat.startForegroundService
import com.example.campus_auto.domain.ServicePeriod
import com.example.campus_auto.ext.toEpochMilli
import com.example.campus_auto.ext.alarmTime

class CampusAutoAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, p1: Intent) {
        val alarmManager = context.getSystemService(ALARM_SERVICE) as AlarmManager
        val triggerTime = ServicePeriod.of().alarmTime().triggerTime

        alarmManager.set(
            AlarmManager.RTC_WAKEUP,
            triggerTime.toEpochMilli(),
            pendingIntent(context)
        )
//        startForegroundService(context, Intent(context, CampusAutomationService::class.java))
    }

    companion object {
        fun pendingIntent(context: Context): PendingIntent {
            val intent = Intent(context, CampusAutoAlarmReceiver::class.java)
            return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        }
    }
}
