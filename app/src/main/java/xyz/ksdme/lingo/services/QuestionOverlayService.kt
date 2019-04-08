package xyz.ksdme.lingo.services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.graphics.Typeface
import android.os.IBinder
import android.support.v4.content.res.ResourcesCompat
import android.util.Log
import android.view.*
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.FrameLayout
import xyz.ksdme.lingo.R
import xyz.ksdme.lingo.components.OptionCheckBox
import xyz.ksdme.lingo.knife.components.StyledRemoteTextView
import xyz.ksdme.lingo.knife.getOverlayType

class QuestionOverlayService: Service(), CompoundButton.OnCheckedChangeListener {

    private val logTag = "QuestionOverlayService"

    private lateinit var fontKarlaRegular: Typeface
    private lateinit var fontKarlaItalics: Typeface
    private lateinit var fontKarlaBold: Typeface

    private lateinit var windowManager: WindowManager
    private lateinit var panel: View

    private lateinit var wordText: StyledRemoteTextView
    private lateinit var wordClass: StyledRemoteTextView
    private lateinit var wordExample: StyledRemoteTextView
    private lateinit var answerOptionA: OptionCheckBox
    private lateinit var answerOptionB: OptionCheckBox
    private lateinit var answerOptionC: OptionCheckBox

    private val rightAnswerIs = 2

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        this.windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        this.getFonts()

        this.drawOverlay()

        this.bindStuff(this.panel)
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

    private fun bindStuff(view: View) {
        this.wordText = view.findViewById(R.id.word_title)
        this.wordClass = view.findViewById(R.id.word_class)
        this.wordExample = view.findViewById(R.id.word_usage_example_text)
        this.answerOptionA = view.findViewById(R.id.answer_a)
        this.answerOptionB = view.findViewById(R.id.answer_b)
        this.answerOptionC = view.findViewById(R.id.answer_c)
    }

    private fun applyInitialMakeUp() {
        this.fontKarlaRegular.let { typeface ->
            this.wordExample.setStyleTypeface(typeface)
            this.wordExample.updateTextColor()

            this.answerOptionA.setStyleTypeface(typeface)
            this.answerOptionA.updateTextColor()

            this.answerOptionB.setStyleTypeface(typeface)
            this.answerOptionB.updateTextColor()

            this.answerOptionC.setStyleTypeface(typeface)
            this.answerOptionC.updateTextColor()
        }

        this.wordText.setStyleTypeface(this.fontKarlaBold)
        this.wordText.updateTextColor()

        this.wordClass.setStyleTypeface(this.fontKarlaItalics)
        this.wordClass.updateTextColor()
    }

    private fun hookOptionCheckBoxes() {
        this.answerOptionA.setOnCheckedChangeListener(this)
        this.answerOptionB.setOnCheckedChangeListener(this)
        this.answerOptionC.setOnCheckedChangeListener(this)
    }

    private fun resolveTagToCheckBox(tag: String): CheckBox? {
        return when(tag) {
            "1" -> this.answerOptionA
            "2" -> this.answerOptionB
            "3" -> this.answerOptionC
            else -> null
        }
    }

    override fun onCheckedChanged(button: CompoundButton?, checked: Boolean) {
        val option = button as OptionCheckBox

        option.updateTextColor()
        option.setOptionStatus(OptionCheckBox.Status.CORRECT)
    }

}
