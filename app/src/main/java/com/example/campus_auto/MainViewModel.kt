package com.example.campus_auto

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campus_auto.data.repository.ConnectionInfoRepository
import com.example.campus_auto.data.repository.ConnectionInfoRepositoryImpl
import com.example.campus_auto.domain.AvailableIp
import com.example.campus_auto.uimodel.ConnectionInfo
import com.example.campus_auto.uimodel.LoadingState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val connectionInfoRepository: ConnectionInfoRepository = ConnectionInfoRepositoryImpl.default()
) : ViewModel() {
    private val _hasAllPermission = MutableStateFlow(false)
    val hasAllPermission: StateFlow<Boolean> = _hasAllPermission.asStateFlow()

    private val _connectionInfo = MutableStateFlow(ConnectionInfo())
    val connectionInfo: StateFlow<ConnectionInfo> = _connectionInfo.asStateFlow()

    private val _loadingState = MutableStateFlow(LoadingState.Loading)
    val loadingState: StateFlow<LoadingState> = _loadingState.asStateFlow()

    private val _isServiceEnabled = MutableStateFlow(false)
    val isServiceEnabled: StateFlow<Boolean> = _isServiceEnabled.asStateFlow()

    private val _event = MutableSharedFlow<MainEvent>()
    val event: SharedFlow<MainEvent> = _event.asSharedFlow()

    private var _hasAccessibilityPermission = false
    private var _hasPostNotificationPermission = false

    init {
        setConnectionInfo()
    }

    fun publishPermissionEvent() {
        viewModelScope.launch {
            if (!_hasAccessibilityPermission) {
                _event.emit(MainEvent.REQUEST_ACCESSIBILITY_PERMISSION)
            }

            if (!_hasPostNotificationPermission) {
                _event.emit(MainEvent.REQUEST_POST_NOTIFICATION_PERMISSION)
            }
        }
    }

    fun setConnectionInfo() {
        viewModelScope.launchLoadable {
            val ipAddress = connectionInfoRepository.ipAddress().getOrNull()

            _connectionInfo.emit(
                ConnectionInfo(
                    ipAddress,
                    AvailableIp(
                        BuildConfig.AVAILABLE_IP_ADDRESS
                    ).isAvailable(ipAddress)
                )
            )
        }
    }

    fun setPermissionState(
        hasAccessibilityPermission: Boolean,
        hasPostNotificationPermission: Boolean,
    ) {
        viewModelScope.launch {
            _hasAccessibilityPermission = hasAccessibilityPermission
            _hasPostNotificationPermission = hasPostNotificationPermission
            _hasAllPermission.emit(
                hasAccessibilityPermission
                        && hasPostNotificationPermission
            )
        }
    }

    fun toggleService() {
        viewModelScope.launch {
            _isServiceEnabled.emit(!_isServiceEnabled.value)
        }
    }

    private fun CoroutineScope.launchLoadable(block: suspend CoroutineScope.() -> Unit) {
        launch {
            _loadingState.emit(LoadingState.Loading)
            block()
            _loadingState.emit(LoadingState.Success)
        }
    }
}
