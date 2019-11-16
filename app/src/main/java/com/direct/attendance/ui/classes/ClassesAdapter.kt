package com.direct.attendance.ui.classes

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.direct.attendance.model.ClassRoom
import com.direct.attendance.ui.base.BaseViewHolder

class ClassesAdapter(
    var classesListener: ClassesListener
): RecyclerView.Adapter<BaseViewHolder>() {

    var items: List<ClassRoom> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return ClassesViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        when(holder) {
            is ClassesViewHolder -> {
                holder.listener = classesListener
                holder.bindTo(items[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun submitList(items: List<ClassRoom>) {
        this.items = items
        notifyDataSetChanged()
    }

}

