package com.direct.attendance.ui.students

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.direct.attendance.R
import com.direct.attendance.model.User
import com.direct.attendance.ui.base.BaseViewHolder
import com.direct.attendance.utils.ModelViewUtils
import com.direct.attendance.utils.OnCustomTouchListener
import kotlinx.android.synthetic.main.item_student.view.cl_item_student
import kotlinx.android.synthetic.main.item_student.view.img_user_picture
import kotlinx.android.synthetic.main.item_student.view.tv_attendance
import kotlinx.android.synthetic.main.item_student.view.tv_user_class
import kotlinx.android.synthetic.main.item_student.view.tv_user_name

class StudentsViewHolder(view: View): BaseViewHolder(view) {

    companion object {
        fun create(parent: ViewGroup): StudentsViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_student, parent, false)
            return StudentsViewHolder(view)
        }
    }

    var listener: StudentsListener? = null

    @SuppressLint("SetTextI18n")
    fun bindTo(user: User) {

        ModelViewUtils.bind(user, view.tv_user_name, view.img_user_picture)

        user.classRoom?.let {
            view.tv_user_class.text = "Class: ${it.name}"
        }

        view.tv_attendance.setTextColor(
            when {
                user.myAttendance.toFloat() >= 85 -> view.context.getColor(R.color.lime_green)
                else -> Color.RED
            }
        )

        view.tv_attendance.text = "${user.myAttendance}%"

        view.cl_item_student.setOnTouchListener(object : OnCustomTouchListener(context = view.context) {

            override fun onClick() {
                super.onClick()
                Navigation.findNavController(view.cl_item_student).navigate(
                    StudentsFragmentDirections.toAttendance(user)
                )
            }

            override fun onLongClick() {
                super.onLongClick()
                listener?.onUpdateStudent(user)
            }
        })
    }
}