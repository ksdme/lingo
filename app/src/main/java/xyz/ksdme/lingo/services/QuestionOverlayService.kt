package xyz.ksdme.lingo.services

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.PixelFormat
import android.graphics.Typeface
import android.os.CountDownTimer
import android.os.IBinder
import android.support.v4.content.res.ResourcesCompat
import android.util.Log
import android.view.*
import android.widget.CompoundButton
import android.widget.FrameLayout
import android.widget.ProgressBar
import kotlinx.android.synthetic.main.service_layout_quiz.view.*
import xyz.ksdme.lingo.R
import xyz.ksdme.lingo.components.OptionCheckBox
import xyz.ksdme.lingo.knife.components.StyledRemoteTextView
import xyz.ksdme.lingo.knife.getOverlayType
import kotlin.math.roundToInt

class QuestionOverlayService: Service(),
    CompoundButton.OnCheckedChangeListener, View.OnTouchListener {

    private val logTag = "QuestionOverlayService"

    private lateinit var fontKarlaRegular: Typeface
    private lateinit var fontKarlaItalics: Typeface
    private lateinit var fontKarlaBold: Typeface

    private lateinit var windowManager: WindowManager
    private lateinit var params: WindowManager.LayoutParams
    private lateinit var panel: View
    private val options = arrayListOf<OptionCheckBox>()

    private val dismissAfterMilliSeconds = 6000L
    private val dismissUpdateEveryMilliSeconds = 10L
    private var dismissCounterHandler: CountDownTimer? = null

    private var _panelInitialY = 0
    private var _panelTouchInitialY = 0.0F

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
        this.params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            getOverlayType(),
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSPARENT
        )

        this.params.gravity = Gravity.TOP.or(Gravity.START)
        this.params.x = 0
        this.params.y = 0

        this.panel = layoutInflate()
        this.panel.setOnTouchListener(this)

        this.windowManager.addView(this.panel, this.params)
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
        this.options.add(view.answer_a)
        this.options.add(view.answer_b)
        this.options.add(view.answer_c)
    }

    private fun applyInitialMakeUp() {
        this.fontKarlaRegular.let { typeface ->
            this.panel.word_usage_example_text.setStyleTypeface(typeface)
            this.panel.word_usage_example_text.updateTextColor()

            this.options.map { option ->
                option.setStyleTypeface(typeface)
                option.updateTextColor()
            }
        }

        this.panel.word_title.setStyleTypeface(this.fontKarlaBold)
        this.panel.word_title.updateTextColor()

        this.panel.word_class.setStyleTypeface(this.fontKarlaItalics)
        this.panel.word_class.updateTextColor()
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

        this.disableOptions()

        this.handleCountDownToDismiss().let { handler ->
            this.panel.dismiss_progress.visibility = View.VISIBLE
            this.dismissCounterHandler = handler
            handler.start()
        }
    }

    private fun disableOptions() {
        this.options.map { option ->
            option.isEnabled = false
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(view: View?, event: MotionEvent?): Boolean {
        return when(event?.action) {
            MotionEvent.ACTION_DOWN -> {
                this._panelInitialY = this.params.y
                this._panelTouchInitialY = event.rawY

                true
            }

            MotionEvent.ACTION_MOVE -> {
                val diffY = event.rawY - this._panelTouchInitialY
                val y = this._panelInitialY + diffY.roundToInt()

                if (view != null) {
                    val heightMax = Resources.getSystem().displayMetrics.heightPixels - view.height

                    if (y < 0 || y > heightMax) {
                        return true
                    }

                    this.params.y = y
                    this.windowManager.updateViewLayout(view, this.params)
                }

                true
            }

            else -> false
        }
    }

    private fun handleCountDownToDismiss(): CountDownTimer {
        val self = this

        return object: CountDownTimer(
            this.dismissAfterMilliSeconds, this.dismissUpdateEveryMilliSeconds) {

            override fun onTick(millisUntilFinished: Long) {
                val ratio = millisUntilFinished.toDouble() / self.dismissAfterMilliSeconds
                self.panel.dismiss_progress.progress = (100 * ratio).toInt()
            }

            override fun onFinish() {
                self.panel.dismiss_progress.visibility = View.INVISIBLE
                self.stopSelf()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        this.dismissCounterHandler?.cancel()
        this.windowManager.removeView(this.panel)
    }

}
