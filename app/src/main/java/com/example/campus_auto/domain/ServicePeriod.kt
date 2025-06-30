package com.example.campus_auto.domain

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

sealed class ServicePeriod(
    val startTime: LocalTime,
    val endTime: LocalTime,
    open val targetDate: LocalDate
) : ServicePeriodRule by DefaultServicePeriodRule() {
    data class GetOnWorkPeriod (
        override val targetDate: LocalDate
    ): ServicePeriod(
        startTimeWhenGetOnWork,
        endTimeWhenGetOnWork,
        targetDate
    )

    data class GetOffWorkPeriod(
        override val targetDate: LocalDate
    ): ServicePeriod(
        startTimeWhenGetOffWork,
        endTimeWhenGetOffWork,
        targetDate
    )

    companion object : ServicePeriodRule by DefaultServicePeriodRule() {
        private fun LocalDate.nextWeekDay(): LocalDate {
            var nextDate = this
            while (!nextDate.isWeekday()) {
                nextDate = nextDate.plusDays(1)
            }
            return this
        }

        private fun LocalDate.isWeekday(): Boolean {
            return when (dayOfWeek) {
                DayOfWeek.SATURDAY -> false
                DayOfWeek.SUNDAY -> false
                else -> true
            }
        }

        fun of(currentTime: LocalDateTime = LocalDateTime.now()): ServicePeriod {
            return if (
                currentTime.toLocalTime().isAfter(endTimeWhenGetOnWork) &&
                currentTime.toLocalTime().isBefore(endTimeWhenGetOffWork)
            ) {
                GetOffWorkPeriod(
                    currentTime.toLocalDate().nextWeekDay()
                )

            } else if (currentTime.toLocalTime().isAfter(endTimeWhenGetOffWork)) {
                GetOnWorkPeriod(
                    currentTime.toLocalDate().plusDays(1).nextWeekDay()
                )
            } else {
                GetOnWorkPeriod(
                    currentTime.toLocalDate().nextWeekDay()
                )
            }
        }
    }
}
