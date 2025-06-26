package com.example.campus_auto.domain

import java.time.LocalTime

data class ServicePeriod(
    val startTime: LocalTime,
    val endTime:LocalTime
) {
    companion object {
        val DEFAULT_START_TIME1 = LocalTime.of(8, 0)
        val DEFAULT_END_TIME1 = LocalTime.of(10, 30)
        val DEFAULT_START_TIME2 = LocalTime.of(18, 0)
        val DEFAULT_END_TIME2 = LocalTime.of(18, 30)
    }
}
