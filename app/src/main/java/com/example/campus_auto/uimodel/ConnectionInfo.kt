package com.example.campus_auto.uimodel

import android.net.LinkProperties
import android.net.wifi.WifiInfo

data class ConnectionInfo(
    override val isLoaded: Boolean = false,
    val ipAddress: String? = null,
    val isEnabled: Boolean = false
) : Reloadable
