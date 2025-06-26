package com.example.campus_auto.data.repository

import android.content.SharedPreferences
import com.example.campus_auto.domain.ServicePeriod
import androidx.core.content.edit

class ServiceScheduleRepositoryImpl(
    private val sharedPreferences: SharedPreferences
) : ServiceScheduleRepository {
    override suspend fun getServiceEnabledState(): Result<Boolean> {
        return runCatching {
            sharedPreferences.getBoolean(KEY_SERVICE_ENABLED, false)
        }
    }

    override suspend fun setServiceEnabledState(isEnabled: Boolean): Result<Unit> {
        return runCatching {
            sharedPreferences.edit { putBoolean(KEY_SERVICE_ENABLED, isEnabled) }
        }
    }

    companion object {
        private const val KEY_SERVICE_ENABLED = "isServiceEnabled"
    }
}
