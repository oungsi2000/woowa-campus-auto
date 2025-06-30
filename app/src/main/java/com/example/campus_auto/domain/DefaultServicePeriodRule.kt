package com.example.campus_auto.domain

import java.time.LocalTime

class DefaultServicePeriodRule : ServicePeriodRule {
    override val startTimeWhenGetOnWork: LocalTime
        get() = LocalTime.of(21, 0)
    override val endTimeWhenGetOnWork: LocalTime
        get() = LocalTime.of(21, 30)
    override val startTimeWhenGetOffWork: LocalTime
        get() = LocalTime.of(23, 0)
    override val endTimeWhenGetOffWork: LocalTime
        get() = LocalTime.of(23, 30)
}
