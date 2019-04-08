package xyz.ksdme.lingo.services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.graphics.Typeface
import android.os.IBinder
import android.support.v4.content.res.ResourcesCompat
import android.view.*
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
    private val options = arrayListOf<OptionCheckBox>()

    private val fakeCorrectAnswer = 1

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
    }

    private fun drawOverlay() {
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

    private fun getFonts() {
        ResourcesCompat.getFont(this, R.font.karla_regular)?.let { this.fontKarlaRegular = it }
        ResourcesCompat.getFont(this, R.font.karla_italic)?.let { this.fontKarlaItalics = it }
        ResourcesCompat.getFont(this, R.font.karla_bold)?.let { this.fontKarlaBold = it }
    }

    private fun bindStuff(view: View) {
        this.wordText = view.findViewById(R.id.word_title)
        this.wordClass = view.findViewById(R.id.word_class)
        this.wordExample = view.findViewById(R.id.word_usage_example_text)

        this.options.add(view.findViewById(R.id.answer_a))
        this.options.add(view.findViewById(R.id.answer_b))
        this.options.add(view.findViewById(R.id.answer_c))
    }

    private fun applyInitialMakeUp() {
        this.fontKarlaRegular.let { typeface ->
            this.wordExample.setStyleTypeface(typeface)
            this.wordExample.updateTextColor()

            this.options.map { option ->
                option.setStyleTypeface(typeface)
                option.updateTextColor()
            }
        }

        this.wordText.setStyleTypeface(this.fontKarlaBold)
        this.wordText.updateTextColor()

        this.wordClass.setStyleTypeface(this.fontKarlaItalics)
        this.wordClass.updateTextColor()
    }

    private fun hookOptionCheckBoxes() {
        this.options.map { option ->
            option.setOnCheckedChangeListener(this)
        }
    }

    override fun onCheckedChanged(button: CompoundButton?, checked: Boolean) {
        val option = button as OptionCheckBox

        option.updateTextColor()
        option.setOptionStatus(OptionCheckBox.Status.WRONG)

        val right = this.options[this.fakeCorrectAnswer]
        right.setOptionStatus(OptionCheckBox.Status.CORRECT)
    }

}
