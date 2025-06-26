package com.example.campus_auto.uimodel

import java.time.LocalDate
import java.time.LocalTime

data class AlarmTime(
    val startTime: LocalTime,
    val endTime: LocalTime,
    val targetDate: LocalDate
)
