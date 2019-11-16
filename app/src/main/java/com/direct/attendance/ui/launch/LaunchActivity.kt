package com.direct.attendance.ui.launch

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.view.animation.AnimationUtils
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.direct.attendance.ui.MainActivity
import com.direct.attendance.R
import com.direct.attendance.model.User
import com.direct.attendance.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_launch.img_launch
import kotlinx.android.synthetic.main.activity_launch.tv_update_attendance
import org.koin.android.scope.currentScope

class LaunchActivity : BaseActivity() {

    private val viewModelFactory: LaunchViewModelFactory by currentScope.inject()

    private lateinit var viewModel: LaunchViewModel
    private var dotsHandler: Handler? = Handler()
    private var loadingDotsCount = 0
    private var students: List<User> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)

        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

        viewModel = ViewModelProvider(this, viewModelFactory).get(LaunchViewModel::class.java)

        viewModel.allStudents.observe(this, Observer {
            if (students.isEmpty()) students = it
        })

        startDotsHandler()
        updateAllStudents()
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
        students.forEach {
            it.setupAttendance()
            viewModel.update(it)
        }

        Handler().postDelayed({
            stopDotsHandler()
            startMainActivity()
        }, 3000)
    }

}