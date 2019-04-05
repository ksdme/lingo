package xyz.ksdme.lingo.services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PixelFormat
import android.graphics.Typeface
import android.os.IBinder
import android.support.v4.content.res.ResourcesCompat
import android.util.Log
import android.view.*
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.FrameLayout
import android.widget.TextView
import xyz.ksdme.lingo.R
import xyz.ksdme.lingo.knife.applyTypefaceOnTextView
import xyz.ksdme.lingo.knife.getOverlayType
import xyz.ksdme.lingo.knife.makeTypefaceText

class QuestionOverlayService: Service(), CompoundButton.OnCheckedChangeListener {

    private val logTag = "QuestionOverlayService"

    private lateinit var fontKarlaRegular: Typeface
    private lateinit var fontKarlaItalics: Typeface
    private lateinit var fontKarlaBold: Typeface

    private lateinit var answerTextColor: ColorStateList

    private lateinit var windowManager: WindowManager
    private lateinit var panel: View

    private lateinit var word: TextView
    private lateinit var wordClass: TextView
    private lateinit var wordExample: TextView
    private lateinit var answerOptionA: CheckBox
    private lateinit var answerOptionB: CheckBox
    private lateinit var answerOptionC: CheckBox

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        this.windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        this.answerTextColor = this.resources.getColorStateList(R.color.color_option_text)
        this.getFonts()

        this.drawOverlay()

        this.inject(this.panel)
        this.applyInitialMakeUp()
        this.hookOptionCheckBoxes()

        Log.d(this.logTag, "onCreated Finished")
    }

    private fun drawOverlay() {
        Log.d(this.logTag, "WindowType: ${getOverlayType()}")

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            getOverlayType(),
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSPARENT
        )

        params.gravity = Gravity.TOP.or(Gravity.START)
        params.x = 0
        params.y = 0

        this.panel = layoutInflate()
        this.windowManager.addView(panel, params)
    }

    private fun layoutInflate(): View {
        val frame = FrameLayout(this)

        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return inflater.inflate(R.layout.service_layout_quiz, frame)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun getFonts() {
        ResourcesCompat.getFont(this, R.font.karla_regular)?.let { this.fontKarlaRegular = it }
        ResourcesCompat.getFont(this, R.font.karla_italic)?.let { this.fontKarlaItalics = it }
        ResourcesCompat.getFont(this, R.font.karla_bold)?.let { this.fontKarlaBold = it }
    }

    private fun applyInitialMakeUp() {
        this.fontKarlaRegular.let { typeface ->
            applyTypefaceOnTextView(this.wordExample, typeface)
            applyTypefaceOnTextView(this.answerOptionA, typeface)
            applyTypefaceOnTextView(this.answerOptionB, typeface)
            applyTypefaceOnTextView(this.answerOptionC, typeface)
        }

        applyTypefaceOnTextView(this.word, this.fontKarlaBold)
        applyTypefaceOnTextView(this.wordClass, this.fontKarlaItalics)
    }

    private fun inject(view: View) {
        this.word = view.findViewById(R.id.word_title)
        this.wordClass = view.findViewById(R.id.word_class)
        this.wordExample = view.findViewById(R.id.word_usage_example_text)
        this.answerOptionA = view.findViewById(R.id.answer_a)
        this.answerOptionB = view.findViewById(R.id.answer_b)
        this.answerOptionC = view.findViewById(R.id.answer_c)
    }

    private fun hookOptionCheckBoxes() {
        this.answerOptionA.setOnCheckedChangeListener(this)
        this.answerOptionB.setOnCheckedChangeListener(this)
        this.answerOptionC.setOnCheckedChangeListener(this)
    }

    override fun onCheckedChanged(button: CompoundButton?, checked: Boolean) {
        // Update text color based on checked state
        button?.text = button?.let {
            val state = intArrayOf((if (checked) 1 else -1) * android.R.attr.state_checked)
            val newColor = this.answerTextColor.getColorForState(state, Color.BLACK)
            makeTypefaceText(this.fontKarlaRegular, button.textSize.toInt(), newColor, button.text.toString())
        }
    }

}
