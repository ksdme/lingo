package xyz.ksdme.lingo.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.widget.Toast

class QuestionOverlayService: Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        Toast.makeText(this,  "I am about to die.", Toast.LENGTH_SHORT).show()

        stopSelf()
    }

}
