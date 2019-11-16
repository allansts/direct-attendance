package com.direct.attendance.ui.attendance

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.direct.attendance.model.Attendance
import com.direct.attendance.ui.base.BaseViewHolder

class AttendanceAdapter(
    var listener: AttendanceListener
) : RecyclerView.Adapter<BaseViewHolder>() {

    var items: List<Attendance> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return AttendanceViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        when(holder) {
            is AttendanceViewHolder -> {
                holder.attendanceListener = listener
                holder.bindTo(items[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun submitList(items: List<Attendance>) {
        this.items = items
        notifyDataSetChanged()
    }
}