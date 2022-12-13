package com.victorhvs.tfc.domain.enums

import java.util.Calendar

sealed class Interval {

    abstract fun periodMilliseconds() : String?
    abstract val period: String
    abstract val interval: String

    companion object {
        fun allIntervals() : List<Interval> {
            return listOf(
                Today(),
                OneWeek(),
                OneMonth(),
                OneYear(),
                Max()
            )
        }
    }

    data class Today(override val period: String = "1D", override val interval: String = "30m") : Interval() {
        override fun periodMilliseconds(): String {
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            return calendar.timeInMillis.toString()
        }
    }

    data class OneWeek(override val period: String = "1W", override val interval: String = "60m") : Interval() {
        override fun periodMilliseconds(): String {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_YEAR, -7)
            return calendar.timeInMillis.toString()
        }
    }

    data class OneMonth(override val period: String = "1M", override val interval: String = "1d") : Interval() {
        override fun periodMilliseconds(): String {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.MONTH, -1)
            return calendar.timeInMillis.toString()
        }
    }

    data class OneYear(override val period: String = "1Y", override val interval: String = "1d") : Interval() {
        override fun periodMilliseconds(): String {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.YEAR, -1)
            return calendar.timeInMillis.toString()
        }
    }

    data class Max(override val period: String = "Máx.", override val interval: String = "1d") : Interval() {
        override fun periodMilliseconds() = null
    }
}
