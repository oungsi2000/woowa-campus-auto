package com.example.campus_auto.domain

data class AvailableIp(
    val ip: String
) {
    fun isAvailable(other:String?): Boolean {
        return ip == other
    }
}
