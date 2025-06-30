package com.example.campus_auto.view.background

import android.app.Application
import android.content.SharedPreferences
import com.example.campus_auto.data.repository.ConnectionInfoRepository
import com.example.campus_auto.data.repository.ConnectionInfoRepositoryImpl
import com.example.campus_auto.data.repository.ServiceScheduleRepository
import com.example.campus_auto.data.repository.ServiceScheduleRepositoryImpl

class CampusAutoApplication : Application() {
    val sharedPreferences: SharedPreferences by lazy {
        getSharedPreferences("campus_auto", MODE_PRIVATE)
    }
    val connectionInfoRepository: ConnectionInfoRepository by lazy {
        ConnectionInfoRepositoryImpl.default()
    }
    val serviceScheduleRepository: ServiceScheduleRepository by lazy {
        ServiceScheduleRepositoryImpl(sharedPreferences)
    }
}
