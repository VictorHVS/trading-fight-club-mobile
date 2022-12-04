package com.victorhvs.tfc.presentation.extensions

import com.google.firebase.Timestamp
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Date?.dateToString(format: String = "dd/MM/yyyy"): String {
    if (this == null) {
        return "--"
    }
    // simple date formatter
    val dateFormatter = SimpleDateFormat(format, Locale.getDefault())

    // return the formatted date string
    return dateFormatter.format(this)
}

const val DATE_FORMAT_SMALL = "dd/MM"
const val DATE_FORMAT_DEFAULT = "dd/MM/yyyy"
const val ZERO = "0"

fun Timestamp.format(pattern: String = DATE_FORMAT_DEFAULT): String =
    this.toDate().format(pattern)

fun Date.format(pattern: String = DATE_FORMAT_DEFAULT): String =
    SimpleDateFormat(pattern).format(this)

fun String.parse(pattern: String = DATE_FORMAT_DEFAULT, default: Date = Date()): Date =
    SimpleDateFormat(pattern).parse(this) ?: default

fun String.toCurrency() = this.toBigDecimalOrZero().toCurrency()

fun String.toBigDecimalOrZero(): BigDecimal =
    try { this.toBigDecimalOrNull().orZero() } catch (e: NumberFormatException) { BigDecimal.ZERO }

fun String?.orZero() = if (this.isNullOrBlank()) ZERO else this

fun String.replaceIf(replacement: String, predicate: () -> Boolean) =
    if (predicate()) replacement else this

fun Number.toFormatedCurrency(showSign: Boolean = false): String {
    val df = DecimalFormat(
        "#,##0.00",
        DecimalFormatSymbols().apply {
            groupingSeparator = '.'
            decimalSeparator = ','
        },
    ).apply {
        isDecimalSeparatorAlwaysShown = true
    }

    val formattedText = df.format(this)
    return if (showSign) {
        val sign = if (this.toDouble() < 0) "" else "+"
        "$sign$formattedText"
    } else {
        formattedText
    }
}
