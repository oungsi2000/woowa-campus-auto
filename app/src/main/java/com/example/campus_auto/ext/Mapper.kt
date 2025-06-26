package com.example.campus_auto.ext

import com.example.campus_auto.domain.ServicePeriod
import com.example.campus_auto.uimodel.AlarmTime

fun AlarmTime.fromDomain(domain: ServicePeriod): AlarmTime {
    return AlarmTime(
        domain.startTime,
        domain.endTime,
        domain.targetDate
    )
}

fun ServicePeriod.toUiModel(): AlarmTime {
    return AlarmTime(
        startTime,
        endTime,
        targetDate
    )
}
