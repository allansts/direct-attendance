package com.direct.attendance.utils

import android.widget.ImageView
import android.widget.TextView
import com.direct.attendance.R
import com.direct.attendance.constant.Gender
import com.direct.attendance.model.User

class ModelViewUtils {

    companion object {
        fun bind(user: User, textView: TextView, imageView: ImageView) {
            bindUserName(user, textView)
            bindUserPicture(user, imageView)
        }

        fun bindUserName(user: User, textView: TextView) {
            textView.text = user.fullname
        }

        fun bindUserPicture(user: User, imageView: ImageView) {
            imageView.setImageResource(
                if(user.gender == Gender.FEMALE.name) R.drawable.ic_female
                else R.drawable.ic_male
            )
        }
    }
}