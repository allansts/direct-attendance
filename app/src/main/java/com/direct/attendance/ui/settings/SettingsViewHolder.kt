package com.direct.attendance.ui.settings

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.View
import com.direct.attendance.R
import com.direct.attendance.ui.base.BaseViewHolder
import kotlinx.android.synthetic.main.item_settings.view.img_settings_next
import kotlinx.android.synthetic.main.item_settings.view.ll_settings_row
import kotlinx.android.synthetic.main.item_settings.view.tv_settings_title

class SettingsViewHolder(view: View, var listener: SettingsListener? = null): BaseViewHolder(view) {

    @SuppressLint("SetTextI18n")
    fun bind(title: Int, isSection: Boolean) {

        view.tv_settings_title.setText(title)

        when(isSection) {
            false -> {
                view.tv_settings_title.setTextAppearance(R.style.SettingsRow)
                view.ll_settings_row.setBackgroundColor(view.context.getColor(R.color.white))
            }
            else -> {
                view.ll_settings_row.setBackgroundColor(Color.TRANSPARENT)
                view.tv_settings_title.setTextAppearance(R.style.SettingsSection)
                view.img_settings_next.visibility = View.INVISIBLE
            }
        }

        view.ll_settings_row.setOnClickListener {
            setOnClick(title)
        }
    }

    private fun setOnClick(title: Int) {
        when(title) {
            R.string.setting_add_class -> onAddClassClick()
            R.string.setting_add_student -> onAddStudentClick()
        }
    }



    private fun onAddClassClick() {
        listener?.onAddClassClick()
    }

    private fun onAddStudentClick() {
        listener?.onAddStudentClick()
    }
}