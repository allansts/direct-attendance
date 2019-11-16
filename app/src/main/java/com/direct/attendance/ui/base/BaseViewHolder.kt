package com.direct.attendance.ui.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import org.koin.core.KoinComponent

open class BaseViewHolder(var view: View): RecyclerView.ViewHolder(view), KoinComponent