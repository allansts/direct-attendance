package com.direct.attendance.constant

enum class DatePatterns(val pattern: String)  {
    ddMMyyyy("dd/MM/yyyy"),
    ddMM("dd/MM"),
    dd("dd"),
    MMMM("MMMM"),
    EEEE("EEEE"),
    yyyy_MM_dd_HHmmss("yyyy-MM-dd HH:mm:ss"),
    yyyy_MM_dd("yyyy-MM-dd"),
    HHmm("HH:mm"),
    ddMMyyyyHHMM("dd/MM/yyyy HH:mm")

}