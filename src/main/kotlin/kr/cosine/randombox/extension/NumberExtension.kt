package kr.cosine.randombox.extension

import java.text.DecimalFormat

private const val DEFAULT_FORMAT = "#,##0.###"
private val decimalFormat = DecimalFormat(DEFAULT_FORMAT)

fun Double.format(): String = decimalFormat.format(this)

fun Int.format(): String = decimalFormat.format(this)

fun Long.format(): String = decimalFormat.format(this)