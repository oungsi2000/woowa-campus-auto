package com.example.campus_auto

import android.accessibilityservice.AccessibilityServiceInfo
import android.app.AlarmManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.view.accessibility.AccessibilityManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.core.app.NotificationManagerCompat
import com.example.campus_auto.ui.MainView
import kotlinx.coroutines.flow.collectLatest
import java.util.Calendar


class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels { MainViewModel.factory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        checkAppInstalled()
        setContent {
            LaunchedEffect(Unit) {
                viewModel.event.collectLatest {
                    when (it) {
                        MainEvent.REQUEST_ACCESSIBILITY_PERMISSION -> {
                            startActivity(
                                Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                            )
                        }

                        MainEvent.REQUEST_POST_NOTIFICATION_PERMISSION -> {
                            startActivity(
                                Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                                    putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
                                }
                            )
                        }
                        MainEvent.START_SERVICE -> {

                        }
                        MainEvent.STOP_SERVICE -> {

                        }
                        MainEvent.RUNNING -> Unit
                    }
                }
            }
            MainView(viewModel)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.setPermissionState(
            hasAccessibilityPermission = hasAccessibilityPermission(),
            hasPostNotificationPermission = hasPostNotificationPermission(),
        )
    }

//    private fun scheduleAlarm() {
//        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
//        val calendar = Calendar.getInstance().apply {
//            timeInMillis = System.currentTimeMillis()
//            set(Calendar.HOUR_OF_DAY, hour)
//            set(Calendar.MINUTE, minute)
//            set(Calendar.SECOND, 0)
//            set(Calendar.MILLISECOND, 0)
//        }
//        alarmManager.set(
//            AlarmManager.RTC_WAKEUP,
//            calendar.timeInMillis,
//            AlarmReceiver.getPendingIntent(this)
//        )
//    }

    private fun checkAppInstalled() {
        runCatching {
            packageManager.getPackageInfo(BuildConfig.CAMPUS_PACKAGE_NAME, PackageManager.GET_ACTIVITIES)
        }.onSuccess {
            viewModel.setAppInstalledState(true)
        }.onFailure {
            viewModel.setAppInstalledState(false)
        }
    }


    private fun hasAccessibilityPermission(): Boolean {
        val accessibilityManager = getSystemService(ACCESSIBILITY_SERVICE) as AccessibilityManager
        return accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK)
            .any {
                it.resolveInfo.serviceInfo.packageName == packageName
            }
    }

    private fun hasPostNotificationPermission(): Boolean {
        val notificationManagerCompat = NotificationManagerCompat.from(this)
        return notificationManagerCompat.areNotificationsEnabled()
    }
}


