package com.direct.attendance.ui.launch

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import com.direct.attendance.ui.MainActivity
import com.direct.attendance.R
import com.direct.attendance.ui.base.BaseActivity

class LaunchActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)

        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

        startMainActivity()
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

}