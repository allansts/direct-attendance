package com.direct.attendance.ui.settings

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.direct.attendance.R
import io.github.luizgrp.sectionedrecyclerviewadapter.Section
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters

class SettingsSection(
    var title: Int,
    var itemList: List<Int>,
    var listener: SettingsListener? = null
): Section(
    SectionParameters.builder()
        .itemResourceId(R.layout.item_settings)
        .headerResourceId(R.layout.item_settings)
        .build()
) {

    override fun getHeaderViewHolder(view: View?): RecyclerView.ViewHolder {
        return SettingsViewHolder(view!!)
    }

    override fun onBindHeaderViewHolder(holder: RecyclerView.ViewHolder?) {
        when(holder) {
            is SettingsViewHolder -> holder.bind(title, true)
        }
    }

    override fun getItemViewHolder(view: View?): RecyclerView.ViewHolder {
        return SettingsViewHolder(view!!, listener)
    }

    override fun onBindItemViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        when(holder) {
            is SettingsViewHolder -> holder.bind(itemList[position], false)
        }
    }

    override fun getContentItemsTotal(): Int {
        return itemList.size
    }

}
