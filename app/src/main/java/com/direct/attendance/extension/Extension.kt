package com.direct.attendance.extension

import android.content.Context
import android.content.pm.PackageManager
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.direct.attendance.R
import com.direct.attendance.constant.DatePatterns
import com.google.android.material.textfield.TextInputLayout
import org.ocpsoft.prettytime.PrettyTime
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

fun Any?.isNull(): Boolean { return this == null }

fun Any?.isNotNull() : Boolean { return this != null }

val NAME_PATTERN : Pattern = Pattern.compile("^[\\p{L} .'-]+$")
var ONLY_DIGITS : Pattern = Pattern.compile("\\d+")

fun Date?.isToday(): Boolean {
    if (this == null) return false
    val today = Date().withoutTime()

    return this.withoutTime() == today
}

fun Date.withoutTime(): Date {
    val cal = Calendar.getInstance()
    cal.time = this
    cal.set(Calendar.HOUR_OF_DAY, 0)
    cal.set(Calendar.MINUTE, 0)
    cal.set(Calendar.SECOND, 0)
    cal.set(Calendar.MILLISECOND, 0)
    return cal.time
}


fun Date.lessThanToday(withoutTime: Boolean = false) : Boolean {
    return when(withoutTime) {
        true -> this.withoutTime() == Date().withoutTime() ||
                this.withoutTime().before(Date().withoutTime())
        else -> this == Date() ||
                this.before(Date())
    }
}

fun Date.daysLeftFrom(date: Date, withoutTime: Boolean = false): Long {
    if (this.isNull()) return -1L

    var dateTemp = date
    if(withoutTime == true) dateTemp = date.withoutTime()

    val diff = this.time - dateTemp.time
    return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)
}

fun Date.monthsLeftFrom(date: Date, withoutTime: Boolean = false): Long {

    var dateTemp = date
    if(withoutTime) dateTemp = date.withoutTime()

    val diff = this.time - dateTemp.time
    return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)/30
}

fun Date?.toString(dtPattern: DatePatterns, locale: Locale) : String {
    this ?: return ""
    val formatter = SimpleDateFormat(dtPattern.pattern, locale)
    return formatter.format(this)
}

fun String?.toPrettyTime(dtPattern: DatePatterns, locale: Locale): String {
    val date = this.toDate(dtPattern = dtPattern, locale = locale) ?: return ""
    return PrettyTime().format(date)
}

fun String?.toDate(dtPattern: DatePatterns, locale: Locale): Date? {
    if (this.isNullOrBlank()) return null

    val parsedDate: Date?
    val formatter = SimpleDateFormat(dtPattern.pattern, locale)

    try {
        parsedDate = formatter.parse(this)
    } catch (e: ParseException) {
        return null
    }

    return parsedDate
}

fun String?.isMention(): Boolean {
    if (this.isNullOrBlank()) return false

    return this.startsWith("@")
}

fun String?.isHashtag(): Boolean {
    if (this.isNullOrBlank()) return false

    return this.startsWith("#")
}

fun AppCompatActivity.informationalDialog(title: String, message: String, positionListener: (() -> Unit)? = null) {
    val builder = AlertDialog.Builder(this, R.style.AlertDialog)
    builder.setTitle(title)
    builder.setMessage(message)
    builder.setCancelable(false)
    builder.setPositiveButton(R.string.ok) { _, _ ->
        positionListener?.invoke()
    }
    builder.show()
}

fun AppCompatActivity.confirmationDialog(title: String, message: String): AlertDialog.Builder {
    val builder = AlertDialog.Builder(this, R.style.AlertDialog)
    builder.setTitle(title)
    builder.setMessage(message)
    return builder
}

fun AppCompatActivity.confirmationDialog(title: Int, message: Int): AlertDialog.Builder {
    return confirmationDialog(getString(title), getString(message))
}

fun AppCompatActivity.errorDialog(message: String) {
    informationalDialog(getString(R.string.warning), message)
}

fun AppCompatActivity.errorDialog(message: String, positionListener: (() -> Unit)? = null) {
    informationalDialog(getString(R.string.warning), message, positionListener)
}

fun AppCompatActivity.isPermissionGranted(permissions: Array<String>) : Boolean {
    var isGranted = -1

    for (permission in permissions) {
        isGranted = ContextCompat.checkSelfPermission(this, permission)
        if (isGranted != PackageManager.PERMISSION_GRANTED) {
            break
        }
    }

    return isGranted == PackageManager.PERMISSION_GRANTED
}

fun Fragment.informationalDialog(context: Context, title: String, message: String) {
    val builder = AlertDialog.Builder(context, R.style.AlertDialog)
    builder.setTitle(title)
    builder.setMessage(message)
    builder.setCancelable(false)
    builder.setPositiveButton(R.string.ok, null)
    builder.show()
}

fun Fragment.confirmationDialog(context: Context, title: String, message: String): AlertDialog.Builder {
    val builder = AlertDialog.Builder(context, R.style.AlertDialog)
    builder.setTitle(title)
    builder.setMessage(message)
    return builder
}

fun Fragment.confirmationDialog(context: Context, title: Int, message: Int): AlertDialog.Builder {
    return confirmationDialog(context, getString(title), getString(message))
}

fun Fragment.errorDialog(context: Context, message: String) {
    informationalDialog(context, getString(R.string.warning), message)
}

fun Fragment.isPermissionGranted(context: Context, permissions: Array<String>) : Boolean {
    var isGranted = -1

    for (permission in permissions) {
        isGranted = ContextCompat.checkSelfPermission(context, permission)
        if (isGranted != PackageManager.PERMISSION_GRANTED) {
            break
        }
    }

    return isGranted == PackageManager.PERMISSION_GRANTED
}

fun Fragment.isNullOrBlank(edit: EditText?, til: TextInputLayout? = null) : Boolean {
    edit?.error = null
    til?.error = null
    if (edit?.text.isNullOrBlank()) {
        if (til.isNotNull()) {
            til?.error = getString(R.string.error_required_field)
            til?.requestFocus()
        } else {
            edit?.error = getString(R.string.error_required_field)
            edit?.requestFocus()
        }
        return true
    }
    return false
}