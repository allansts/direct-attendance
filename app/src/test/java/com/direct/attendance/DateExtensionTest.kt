package com.direct.attendance

import com.direct.attendance.constant.Constants.Companion.BR_LOCALE
import com.direct.attendance.constant.DatePatterns
import com.direct.attendance.extension.isToday
import com.direct.attendance.extension.toString
import com.direct.attendance.extension.withoutTime
import java.util.Date
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class DateExtensionTest {

    @Test
    fun `date today`() {
        val value = Date().isToday()
        val expectedValue = true
        val illegalValue = false

        assertEquals(value, expectedValue)
        assertNotEquals(illegalValue, value)
    }

    @Test
    fun `date without time`() {
        val value = Date().withoutTime()
        val expectedValue = "00:00"
        val illegalValue = "10:30"

        assertEquals(expectedValue, value.toString(DatePatterns.HHmm, BR_LOCALE))
        assertNotEquals(illegalValue, value.toString(DatePatterns.HHmm, BR_LOCALE))
    }
}