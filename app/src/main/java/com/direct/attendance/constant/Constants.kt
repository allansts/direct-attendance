package com.direct.attendance.constant

import java.util.Locale

class Constants private constructor() {

    companion object {
        val BR_LOCALE = Locale("pt", "BR")

        const val YOUTUBE_BASE_URL = "https://www.youtube.com/watch?v="

        const val FORCE_UPDATE_CAN_CLOSE = "can_close"
        const val FORCE_UPDATE_UPDATE_URL = "update_url"
    }
}