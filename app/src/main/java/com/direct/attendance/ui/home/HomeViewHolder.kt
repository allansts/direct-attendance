package com.direct.attendance.ui.home

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.ColorUtils
import com.direct.attendance.R
import com.direct.attendance.model.User
import com.direct.attendance.ui.base.BaseViewHolder
import com.direct.attendance.utils.ModelViewUtils
import kotlinx.android.synthetic.main.item_home.view.img_user_picture
import kotlinx.android.synthetic.main.item_home.view.tv_attendance
import kotlinx.android.synthetic.main.item_home.view.tv_user_class
import kotlinx.android.synthetic.main.item_home.view.tv_user_name

class HomeViewHolder(view: View): BaseViewHolder(view) {

    companion object {
        fun create(parent: ViewGroup): HomeViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_home, parent, false)
            return HomeViewHolder(view)
        }
    }

    @SuppressLint("SetTextI18n")
    fun bindTo(user: User) {

        ModelViewUtils.bind(user, view.tv_user_name, view.img_user_picture)

        user.classRoomObj?.let {
            view.tv_user_class.text = "Class: ${it.name}"
        }

        view.tv_attendance.setTextColor(
            ColorUtils.blendARGB(Color.RED, Color.GREEN, user.myAttendance.toFloat())
        )

        view.tv_attendance.text = "${user.myAttendance}%"
    }
}