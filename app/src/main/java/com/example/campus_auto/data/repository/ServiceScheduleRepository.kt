package com.example.campus_auto.data.repository

import com.example.campus_auto.domain.ServicePeriod

interface ServiceScheduleRepository {
    suspend fun getServiceEnabledState(): Result<Boolean>
    suspend fun setServiceEnabledState(isEnabled: Boolean): Result<Unit>
}
