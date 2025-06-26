package com.example.campus_auto.domain

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

sealed class ServicePeriod(
    val startTime: LocalTime,
    val endTime: LocalTime,
) : ServicePeriodRule by DefaultServicePeriodRule() {
    abstract fun nextPeriod(): ServicePeriod

    data class GetOnWorkPeriod (
        val targetDate: LocalDate
    ): ServicePeriod(
        startTimeWhenGetOnWork,
        endTimeWhenGetOnWork,
    ) {
        override fun nextPeriod(): ServicePeriod {
            return GetOffWorkPeriod(targetDate.nextWeekDay())
        }
    }

    data class GetOffWorkPeriod(
        val targetDate: LocalDate
    ): ServicePeriod(
        startTimeWhenGetOffWork,
        endTimeWhenGetOffWork,
    ) {
        override fun nextPeriod(): ServicePeriod {
            return GetOnWorkPeriod(targetDate.plusDays(1).nextWeekDay())
        }
    }

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
                GetOnWorkPeriod(
                    currentTime.toLocalDate().nextWeekDay()
                )

            } else {
                GetOnWorkPeriod(
                    currentTime.toLocalDate().nextWeekDay()
                )
            }
        }
    }
}
