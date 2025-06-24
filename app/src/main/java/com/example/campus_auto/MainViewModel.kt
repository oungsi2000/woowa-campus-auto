package com.example.campus_auto

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _hasAllPermission = MutableStateFlow(false)
    val hasAllPermission: StateFlow<Boolean> = _hasAllPermission.asStateFlow()

    private var _hasAccessibilityPermission = false
    private var _hasPostNotificationPermission = false

    private val _event = MutableSharedFlow<MainEvent>()
    val event: SharedFlow<MainEvent> = _event.asSharedFlow()

    fun setAccessibilityPermission() {
        viewModelScope.launch {
            if (!_hasAccessibilityPermission) {
                _event.emit(MainEvent.REQUEST_ACCESSIBILITY_PERMISSION)
            }

            if (!_hasPostNotificationPermission) {
                _event.emit(MainEvent.REQUEST_POST_NOTIFICATION_PERMISSION)
            }
        }
    }

    fun setPermissionState(
        hasAccessibilityPermission: Boolean,
        hasPostNotificationPermission: Boolean
    ) {
        viewModelScope.launch {
            _hasAccessibilityPermission = hasAccessibilityPermission
            _hasPostNotificationPermission = hasPostNotificationPermission
            _hasAllPermission.emit(hasAccessibilityPermission && hasPostNotificationPermission)
        }
    }

}
