package xyz.ksdme.lingo.knife.components

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.TextView
import xyz.ksdme.lingo.knife.makeTypefaceText

class StyledRemoteTextView: TextView {

    constructor(context: Context): super(context)
    constructor(context: Context, attrs: AttributeSet?): super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs, defStyleAttr)

    private lateinit var font: Typeface

    fun setStyleTypeface(font: Typeface){
        this.font = font
    }

    @JvmOverloads
    fun setStyledText(text: CharSequence, font: Typeface? = null) {
        var fontAdjusted = this.font

        if (font != null) {
            fontAdjusted = font
        }

        this.text = makeTypefaceText(fontAdjusted, this.textSize.toInt(), this.currentTextColor, text.toString())
    }

    fun updateTextColor() {
        this.setStyledText(this.text.toString(), this.font)
    }

}
