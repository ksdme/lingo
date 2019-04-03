package xyz.ksdme.lingo.services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.IBinder
import android.support.v4.content.res.ResourcesCompat
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import android.widget.TextView
import xyz.ksdme.lingo.R
import xyz.ksdme.lingo.knife.applyTypefaceOnTextView
import xyz.ksdme.lingo.knife.getOverlayType

class QuestionOverlayService: Service() {
    private val logTag = "QuestionOverlayService"

    private lateinit var windowManager: WindowManager
    private lateinit var panel: View

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        this.windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        this.drawOverlay()

        val word = this.panel.findViewById<TextView>(R.id.word_title)
        val typeface = ResourcesCompat.getFont(this, R.font.karla_bold)

        typeface?.apply {
            applyTypefaceOnTextView(word, this)
        }

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

}
