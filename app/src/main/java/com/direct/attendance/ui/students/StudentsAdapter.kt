package com.direct.attendance.ui.students

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.direct.attendance.model.ClassRoom
import com.direct.attendance.model.User
import com.direct.attendance.ui.base.BaseViewHolder

class StudentsAdapter(
    var studentsListener: StudentsListener
) : RecyclerView.Adapter<BaseViewHolder>() {

    private var items: List<User> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return StudentsViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        when(holder) {
            is StudentsViewHolder -> {
                holder.listener = studentsListener
                holder.bindTo(items[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun submitList(items: List<User>, classes: List<ClassRoom>? = null) {
        this.items = items

        this.items.forEach { user ->
            user.classRoom = classes?.firstOrNull { cl ->
                cl.id == user.classRoomId
            }
        }

        notifyDataSetChanged()
    }
}