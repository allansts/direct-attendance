package com.direct.attendance.ui.launch

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.WindowManager
import android.view.animation.AnimationUtils
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.direct.attendance.R
import com.direct.attendance.constant.Constants.Companion.BR_LOCALE
import com.direct.attendance.constant.DatePatterns
import com.direct.attendance.extension.isToday
import com.direct.attendance.extension.toDate
import com.direct.attendance.extension.toString
import com.direct.attendance.model.User
import com.direct.attendance.ui.MainActivity
import com.direct.attendance.ui.base.BaseActivity
import com.direct.attendance.utils.SharedUtils
import kotlinx.android.synthetic.main.activity_launch.img_launch
import kotlinx.android.synthetic.main.activity_launch.tv_update_attendance
import org.koin.android.scope.currentScope
import java.util.Date

class LaunchActivity : BaseActivity() {

    private val viewModelFactory: LaunchViewModelFactory by currentScope.inject()

    private lateinit var viewModel: LaunchViewModel
    private lateinit var observer: Observer<List<User>>
    private var dotsHandler: Handler? = Handler()
    private var loadingDotsCount = 0
    private var students: List<User> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)

        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

        viewModel = ViewModelProvider(this, viewModelFactory).get(LaunchViewModel::class.java)

        observer = Observer {
            Log.d("Launch", "All Student Observer")
            if (students.isEmpty()) {
                students = it
                updateAllStudents()
            }
        }

        viewModel.allStudents.observe(this, observer)

        startDotsHandler()
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun startDotsHandler() {
        val pulse = AnimationUtils.loadAnimation(this, R.anim.pulse_dreams)
        img_launch.startAnimation(pulse)

        dotsHandler?.post(object : Runnable {
            override fun run() {
                val text = when (loadingDotsCount % 4) {
                    0 -> "${getString(R.string.launch_update_attendance)} .  "
                    1 -> "${getString(R.string.launch_update_attendance)} .. "
                    2 -> "${getString(R.string.launch_update_attendance)} ..."
                    else -> "${getString(R.string.launch_update_attendance)}    "
                }

                tv_update_attendance.post { tv_update_attendance.text = text }
                loadingDotsCount += 1
                dotsHandler?.postDelayed(this, 350)
            }
        })
    }

    private fun stopDotsHandler() {
        img_launch.clearAnimation()
        dotsHandler?.removeCallbacksAndMessages(null)
        dotsHandler = null
    }

    private fun updateAllStudents() {
        viewModel.allStudents.removeObserver(observer)
        val updateDate = SharedUtils.loadUpdateDate(this).toDate(DatePatterns.ddMMyyyy, BR_LOCALE)

        when(updateDate.isToday()) {
            false -> {
                SharedUtils.saveUpdateDate(this, Date().toString(DatePatterns.ddMMyyyy, BR_LOCALE))
                students.forEach {
                    it.setupAttendance()
                    viewModel.update(it)
                }
            }
        }

        Handler().postDelayed({
            stopDotsHandler()
            startMainActivity()
        }, 3000)
    }

}