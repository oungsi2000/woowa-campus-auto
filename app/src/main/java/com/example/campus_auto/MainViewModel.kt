package com.example.campus_auto

import android.content.pm.PackageManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _hasAllPermission = MutableStateFlow(false)
    val hasAllPermission: StateFlow<Boolean> = _hasAllPermission.asStateFlow()

    private val _event = MutableSharedFlow<MainEvent>()
    val event: SharedFlow<MainEvent> = _event.asSharedFlow()

    fun setPermission() {
        viewModelScope.launch {
            _event.emit(MainEvent.REQUEST_ACCESSIBILITY_PERMISSION)
        }
    }
}
