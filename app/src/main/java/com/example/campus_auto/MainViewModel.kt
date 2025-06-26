package com.example.campus_auto

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.campus_auto.data.repository.ConnectionInfoRepository
import com.example.campus_auto.data.repository.ConnectionInfoRepositoryImpl
import com.example.campus_auto.data.repository.ServiceScheduleRepository
import com.example.campus_auto.domain.AvailableIp
import com.example.campus_auto.ext.combineLoadingState
import com.example.campus_auto.uimodel.ConnectionInfo
import com.example.campus_auto.uimodel.LoadingState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

class MainViewModel(
    private val connectionInfoRepository: ConnectionInfoRepository,
    private val serviceScheduleRepository: ServiceScheduleRepository
) : ViewModel() {
    private val _hasAllPermission = MutableStateFlow(false)
    val hasAllPermission: StateFlow<Boolean> = _hasAllPermission.asStateFlow()

    private val _connectionInfo = MutableStateFlow(ConnectionInfo())
    val connectionInfo: StateFlow<ConnectionInfo> = _connectionInfo.asStateFlow()

    private val _connectionLoadingState = MutableStateFlow(LoadingState.Loading)
    private val _serviceLoadingState = MutableStateFlow(LoadingState.Loading)

    val loadingState: StateFlow<LoadingState> = combineLoadingState(
        viewModelScope,
        _serviceLoadingState,
        _connectionLoadingState
    )

    private val _isServiceEnabled = MutableStateFlow(false)
    val isServiceEnabled: StateFlow<Boolean> = _isServiceEnabled.asStateFlow()

    private val _isAppInstalled = MutableStateFlow(false)
    val isAppInstalled: StateFlow<Boolean> = _isAppInstalled.asStateFlow()

    private val _event = MutableSharedFlow<MainEvent>()
    val event: SharedFlow<MainEvent> = _event.asSharedFlow()

    private var _hasAccessibilityPermission = false
    private var _hasPostNotificationPermission = false

    init {
        viewModelScope.launch {
            initServiceState()
            delay(1000)
            setConnectionInfo()
        }
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
        viewModelScope.launchLoadable(_connectionLoadingState) {
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

    fun setAppInstalledState(isAppInstalled: Boolean) {
        viewModelScope.launch {
            _isAppInstalled.emit(isAppInstalled)
        }
    }

    fun toggleService() {
        viewModelScope.launch {
            if (_isServiceEnabled.value) {
                _event.emit(MainEvent.STOP_SERVICE)
            } else {
                _event.emit(MainEvent.START_SERVICE)
            }
            serviceScheduleRepository.setServiceEnabledState(!_isServiceEnabled.value)
            _isServiceEnabled.emit(!_isServiceEnabled.value)
        }
    }

    private fun initServiceState() {
        viewModelScope.launchLoadable(_serviceLoadingState) {
            _isServiceEnabled.emit(
                serviceScheduleRepository.getServiceEnabledState().getOrDefault(false)
            )
        }
    }

    private fun CoroutineScope.launchLoadable(state: MutableStateFlow<LoadingState>, block: suspend CoroutineScope.() -> Unit) {
        launch {
            state.emit(LoadingState.Loading)
            block()
            state.emit(LoadingState.Success)
        }
    }

    companion object {
        fun factory(): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val application =
                        (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as CampusAutoApplication)
                    MainViewModel(
                        application.connectionInfoRepository,
                        application.serviceScheduleRepository
                    )
                }
            }
    }
}
