package com.example.campus_auto.data.repository

interface ConnectionInfoRepository {
    suspend fun ipAddress(): Result<String>
}
