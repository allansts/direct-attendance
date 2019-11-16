package com.direct.attendance.ui.base

import androidx.fragment.app.Fragment
import com.kaopiz.kprogresshud.KProgressHUD
import kotlin.properties.Delegates

open class BaseFragment: Fragment() {

    private var progress: KProgressHUD? = null

    protected var isLoading: Boolean by Delegates.observable(false) { _, _, newValue ->
        if (newValue) showProgress() else dismissProgress()
    }

    private fun initProgressHud() {
        context?.let {
            progress = KProgressHUD
                .create(it)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setDimAmount(0.5f)
                .setCancellable(false)
        }
    }

    protected fun showProgress() {
        if (progress == null) initProgressHud()

        progress?.show()
    }

    protected fun dismissProgress() {
        progress?.dismiss()
    }

}