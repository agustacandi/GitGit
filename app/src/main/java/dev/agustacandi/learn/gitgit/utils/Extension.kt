package dev.agustacandi.learn.gitgit.utils

import java.text.DecimalFormat

fun Int?.toDecimalFormat(): String {
    val df = DecimalFormat("#,###")
    return df.format(this)
}