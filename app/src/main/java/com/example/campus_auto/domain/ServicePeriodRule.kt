package com.example.campus_auto.domain

import java.time.LocalTime

interface ServicePeriodRule {
    val startTimeWhenGetOnWork: LocalTime
    val endTimeWhenGetOnWork : LocalTime
    val startTimeWhenGetOffWork : LocalTime
    val endTimeWhenGetOffWork: LocalTime
}
