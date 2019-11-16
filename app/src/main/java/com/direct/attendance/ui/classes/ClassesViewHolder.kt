package com.direct.attendance.ui.classes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.direct.attendance.R
import com.direct.attendance.model.ClassRoom
import com.direct.attendance.ui.base.BaseViewHolder
import com.direct.attendance.utils.OnCustomTouchListener
import kotlinx.android.synthetic.main.item_class.view.ll_item_class
import kotlinx.android.synthetic.main.item_class.view.tv_class_name

class ClassesViewHolder(view: View): BaseViewHolder(view) {

    companion object {
        fun create(parent: ViewGroup): ClassesViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_class, parent, false)
            return ClassesViewHolder(view)
        }
    }

    var listener: ClassesListener? = null

    fun bindTo(classRoom: ClassRoom) {

        view.tv_class_name.text = classRoom.name
        view.ll_item_class.setOnTouchListener(object : OnCustomTouchListener(context = view.context) {

            override fun onClick() {
                super.onClick()
                Navigation.findNavController(view.ll_item_class).navigate(
                    ClassesFragmentDirections.toStudents(classRoom.id)
                )
            }

            override fun onLongClick() {
                super.onLongClick()
                listener?.onUpdateClass(classRoom)
            }
        })
    }
}