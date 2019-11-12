package com.direct.attendance.ui.home

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.direct.attendance.model.User
import com.direct.attendance.ui.base.BaseViewHolder

class HomeAdapter : RecyclerView.Adapter<BaseViewHolder>() {

    private var items: List<User> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return HomeViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        when(holder) {
            is HomeViewHolder -> holder.bindTo(items[position])
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun submitList(items: List<User>) {
        this.items = items
        notifyDataSetChanged()
    }
}