package com.example.campus_auto.domain

import java.time.LocalTime

class DefaultServicePeriodRule : ServicePeriodRule {
    override val startTimeWhenGetOnWork: LocalTime
        get() = LocalTime.of(8, 0)
    override val endTimeWhenGetOnWork: LocalTime
        get() = LocalTime.of(10, 30)
    override val startTimeWhenGetOffWork: LocalTime
        get() = LocalTime.of(18, 0)
    override val endTimeWhenGetOffWork: LocalTime
        get() = LocalTime.of(18, 30)
}
