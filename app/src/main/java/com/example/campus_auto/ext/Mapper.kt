package com.example.campus_auto.ext

import com.example.campus_auto.domain.ServicePeriod
import com.example.campus_auto.uimodel.AlarmTime
import java.time.LocalDateTime
import java.time.LocalTime

fun ServicePeriod.alarmTime(now: LocalTime = LocalTime.now()): AlarmTime {
    return if (now.isAfter(startTime) && now.isBefore(endTime)) {
        AlarmTime(
            endTime.atDate(targetDate),
        )
    } else AlarmTime(
        startTime.atDate(targetDate),
    )
}
