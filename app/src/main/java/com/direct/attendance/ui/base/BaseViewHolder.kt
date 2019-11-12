package com.direct.attendance.ui.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView

open class BaseViewHolder(var view: View): RecyclerView.ViewHolder(view) {

    open fun setupViewVisibility() {}
}