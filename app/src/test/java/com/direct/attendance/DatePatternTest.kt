package com.direct.attendance

import com.direct.attendance.constant.Constants.Companion.BR_LOCALE
import com.direct.attendance.constant.DatePatterns
import com.direct.attendance.extension.toString
import com.direct.attendance.extension.withoutTime
import org.junit.Test
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.test.assertEquals

class DatePatternTest {

    @Test
    fun `date pattern ddmmyyyy`() {
        val value = `get date 30 January 2019`().toString(DatePatterns.ddMMyyyy, BR_LOCALE)
        val expectedValue = "30/01/2019"
        assertEquals(expectedValue, value)
    }

    @Test
    fun `date pattern ddmm`() {
        val value = `get date 30 January 2019`().toString(DatePatterns.ddMM, BR_LOCALE)
        val expectedValue = "30/01"
        assertEquals(expectedValue, value)
    }

    @Test
    fun `date pattern dd`() {
        val value = `get date 30 January 2019`().toString(DatePatterns.dd, BR_LOCALE)
        val expectedValue = "30"
        assertEquals(expectedValue, value)
    }

    @Test
    fun `date pattern MMMM`() {
        val value = `get date 30 January 2019`().toString(DatePatterns.MMMM, Locale.US)
        val expectedValue = "January"
        assertEquals(expectedValue, value)
    }

    @Test
    fun `date pattern EEEE`() {
        val value = `get date 30 January 2019`().toString(DatePatterns.EEEE, Locale.US)
        val expectedValue = "Wednesday"
        assertEquals(expectedValue, value)
    }

    @Test
    fun `date pattern yyyy-MM-dd HHmmss`() {
        val value = `get date 30 January 2019`().toString(DatePatterns.yyyy_MM_dd_HHmmss, BR_LOCALE)
        val expectedValue = "2019-01-30 00:00:00"
        assertEquals(expectedValue, value)
    }

    @Test
    fun `date pattern yyyy-MM-dd`() {
        val value = `get date 30 January 2019`().toString(DatePatterns.yyyy_MM_dd, BR_LOCALE)
        val expectedValue = "2019-01-30"
        assertEquals(expectedValue, value)
    }

    @Test
    fun `date pattern HHmm`() {
        val value = `get date 30 January 2019`().toString(DatePatterns.HHmm, BR_LOCALE)
        val expectedValue = "00:00"
        assertEquals(expectedValue, value)
    }

    @Test
    fun `date pattern ddmmyyyy HHmm`() {
        val value = `get date 30 January 2019`().toString(DatePatterns.ddMMyyyyHHMM, BR_LOCALE)
        val expectedValue = "30/01/2019 00:00"
        assertEquals(expectedValue, value)
    }

    fun `get date 30 January 2019`(): Date {
        val calendar = Calendar.getInstance()
        calendar.set(2019, Calendar.JANUARY, 30)
        return calendar.time.withoutTime()
    }
}