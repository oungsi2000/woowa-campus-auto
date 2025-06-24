package com.example.campus_auto

import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.accessibility.AccessibilityManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.flow.collectLatest

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LaunchedEffect(Unit) {
                viewModel.event.collectLatest {
                    when (it) {
                        MainEvent.REQUEST_ACCESSIBILITY_PERMISSION -> {
                            startActivity(
                                Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                            )
                        }
                        MainEvent.RUNNING -> Unit
                    }
                }
            }
            MainView(viewModel)
        }
    }

    private fun checkPermission() {
//        val accessibilityManager = getSystemService(ACCESSIBILITY_SERVICE) as AccessibilityManager
//        accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC)
//            .filter {
//
//            }
        //TODO 접근성 권한 체크 기능 구현
    }
}


