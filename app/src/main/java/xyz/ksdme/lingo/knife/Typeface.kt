package xyz.ksdme.lingo.knife

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.widget.TextView
import xyz.ksdme.lingo.knife.components.CustomTypefaceSpan

fun makeTypefaceText(typeface: Typeface,
                     textSize: Int,
                     textColor: Int,
                     text: String): SpannableString {

    val spanned = SpannableString(text)
    val fontSpan = CustomTypefaceSpan("", typeface, textSize, textColor)

    spanned.setSpan(fontSpan, 0, text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    return spanned
}

fun setTypefaceTextOnTextView(textView: TextView, typeface: Typeface, text: String) {
    textView.text = makeTypefaceText(
        typeface, textView.textSize.toInt(), textView.currentTextColor, text)
}

fun applyTypefaceOnTextView(textView: TextView, typeface: Typeface) {
    setTypefaceTextOnTextView(textView, typeface, textView.text.toString())
}
