package xyz.ksdme.lingo.components

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import android.widget.CheckBox
import xyz.ksdme.lingo.R
import xyz.ksdme.lingo.knife.makeTypefaceText

class OptionCheckBox: CheckBox {

    enum class Status {
        NONE,
        WRONG,
        CORRECT,
    }

    constructor(context: Context): super(context)
    constructor(context: Context, attrs: AttributeSet?): super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs, defStyleAttr)

    private var currentStatus: Status? = null

    private lateinit var font: Typeface
    private val optionTextColor: ColorStateList = this.context.resources.getColorStateList(
        R.color.color_option_text)

    override fun onCreateDrawableState(extraSpace: Int): IntArray {
        val newState = when(this.currentStatus) {
            Status.WRONG -> intArrayOf(R.attr.answer_wrong, -R.attr.answer_correct)
            Status.CORRECT -> intArrayOf(-R.attr.answer_wrong, R.attr.answer_correct)
            else -> intArrayOf(-R.attr.answer_wrong, -R.attr.answer_correct)
        }

        val drawableState = super.onCreateDrawableState(extraSpace + 2)
        View.mergeDrawableStates(drawableState, newState)
        return drawableState
    }

    fun setStyleTypeface(font: Typeface){
        this.font = font
    }

    @JvmOverloads
    fun setStyledText(text: CharSequence, font: Typeface? = null) {
        var fontAdjusted = this.font

        if (font != null) {
            fontAdjusted = font
        }

        val state = intArrayOf((if (this.isChecked) 1 else -1) * android.R.attr.state_checked)
        val newColor = this.optionTextColor.getColorForState(state, Color.BLACK)

        this.text = makeTypefaceText(fontAdjusted, this.textSize.toInt(), newColor, text.toString())
    }

    fun updateTextColor() {
        this.setStyledText(this.text.toString(), this.font)
    }

    fun setOptionStatus(status: OptionCheckBox.Status) {
        this.currentStatus = status
        this.refreshDrawableState()
    }

}
