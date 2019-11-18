package com.direct.attendance.ui.attendance

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.direct.attendance.R
import com.direct.attendance.constant.ClassTime
import com.direct.attendance.constant.Constants.Companion.BR_LOCALE
import com.direct.attendance.constant.Constants.Companion.classTimeAfternoon
import com.direct.attendance.constant.Constants.Companion.classTimeMorning
import com.direct.attendance.constant.DatePatterns
import com.direct.attendance.extension.toString
import com.direct.attendance.model.Attendance
import com.direct.attendance.ui.base.BaseViewHolder
import kotlinx.android.synthetic.main.item_attendance.view.cb_first
import kotlinx.android.synthetic.main.item_attendance.view.cb_second
import kotlinx.android.synthetic.main.item_attendance.view.cb_third
import kotlinx.android.synthetic.main.item_attendance.view.switch_attendance
import kotlinx.android.synthetic.main.item_attendance.view.tv_attendance_date
import kotlinx.android.synthetic.main.item_attendance.view.tv_disable_all
import kotlinx.android.synthetic.main.item_attendance.view.tv_weekend

class AttendanceViewHolder(view: View): BaseViewHolder(view) {

    companion object {
        fun create(parent: ViewGroup): AttendanceViewHolder {
            val view = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_attendance, parent, false)
            return AttendanceViewHolder(view)
        }
    }

    var attendanceListener: AttendanceListener? = null
    private lateinit var attendance: Attendance

    @SuppressLint("DefaultLocale")
    fun bindTo(attendance: Attendance) {
        this.attendance = attendance

        view.tv_attendance_date.text = attendance.date.toString(DatePatterns.ddMMyyyy, BR_LOCALE)
        view.tv_weekend.text = attendance.date.toString(DatePatterns.EEEE, BR_LOCALE).capitalize()

        setupEnableViews(attendance)
        setupClassTimeChecked(attendance.presents)

        when(attendance.time) {
            ClassTime.MORNING.name -> setupClassTime(classTimeMorning)
            ClassTime.AFTERNOON.name -> setupClassTime(classTimeAfternoon)
        }


        view.cb_first.setOnCheckedChangeListener { _, isChecked ->
            updateAttendance(isChecked, 0)
            attendanceListener?.updateUser()
        }

        view.cb_second.setOnCheckedChangeListener { _, isChecked ->
            updateAttendance(isChecked, 1)
            attendanceListener?.updateUser()
        }

        view.cb_third.setOnCheckedChangeListener { _, isChecked ->
            updateAttendance(isChecked, 2)
            attendanceListener?.updateUser()
        }

        view.switch_attendance.setOnCheckedChangeListener { _, isOn ->
            updateCheckBoxes(!isOn)
        }

    }

    private fun setupEnableViews(attendance: Attendance) {
        view.tv_disable_all.visibility = if (attendance.enable) View.VISIBLE else View.GONE
        view.switch_attendance.visibility = if (attendance.enable) View.VISIBLE else View.GONE

        view.cb_first.isEnabled = attendance.enable
        view.cb_second.isEnabled = attendance.enable
        view.cb_third.isEnabled = attendance.enable
    }

    private fun setupClassTime(times: List<String>) {
        view.cb_first.text = times[0]
        view.cb_second.text = times[1]
        view.cb_third.text = times[2]
    }

    private fun setupClassTimeChecked(presents: List<Int>) {
        view.cb_first.isChecked = presents[0] == 1
        view.cb_second.isChecked = presents[1] == 1
        view.cb_third.isChecked = presents[2] == 1

        view.switch_attendance.isChecked = presents.filter { it == 1 }.size != 3
    }

    private fun updateCheckBoxes(isChecked: Boolean) {
        view.cb_first.isChecked = isChecked
        view.cb_second.isChecked = isChecked
        view.cb_third.isChecked = isChecked
    }

    private fun updateAttendance(isChecked: Boolean, position: Int) {
        attendance.presents[position] = if (isChecked) 1 else 0
    }
}