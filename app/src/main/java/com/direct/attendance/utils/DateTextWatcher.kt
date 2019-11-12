package com.direct.attendance.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.util.Calendar
import java.util.GregorianCalendar

class DateTextWatcher(var editText: EditText?) : TextWatcher {

    private var current = ""
    private val ddmmyyyy = "DDMMYYYY"

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
    override fun afterTextChanged(s: Editable) {}

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        if (s.toString() != current) {
            var clean = s.toString().replace("[^\\d.]".toRegex(), "")
            val cleanC = current.replace("[^\\d.]".toRegex(), "")

            val cl = clean.length
            var sel = cl
            var i = 2
            while (i <= cl && i < 6) {
                sel++
                i += 2
            }

            //Fix for pressing delete next to a forward slash
            if (clean == cleanC) sel--

            if (clean.length < 8) {
                clean += ddmmyyyy.substring(clean.length)
            } else {
                //This part makes sure that when we finish entering numbers
                //the date is correct, fixing it otherways
                var day = Integer.parseInt(clean.substring(0, 2))
                var mon = Integer.parseInt(clean.substring(2, 4))
                var year = Integer.parseInt(clean.substring(4, 8))

                if (mon > 12) mon = 12 else if (mon < 1) mon = 1
                val cal = GregorianCalendar()
                cal.set(Calendar.MONTH, mon - 1)

                val currentYear = cal.get(Calendar.YEAR)

                if (day > cal.getActualMaximum(Calendar.DATE)) day = cal.getActualMaximum(Calendar.DATE)
                else if (day < cal.getActualMinimum(Calendar.DATE)) day = cal.getActualMinimum(Calendar.DATE)

                year = if (year < 1900) 1900 else if (year > currentYear) currentYear else year
                clean = String.format("%02d%02d%02d", day, mon, year)
            }

            clean = String.format(
                "%s/%s/%s", clean.substring(0, 2),
                clean.substring(2, 4),
                clean.substring(4, 8)
            )
            current = clean
            editText?.setText(current)
            editText?.setSelection(if (sel < current.length) sel else current.length)
        }
    }
}