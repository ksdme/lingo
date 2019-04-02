package xyz.ksdme.lingo

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import xyz.ksdme.lingo.knife.doesRequireOverlayAbility
import xyz.ksdme.lingo.knife.requestOverlayAbility
import xyz.ksdme.lingo.knife.safelyCheckIfHasOverlayAbility
import xyz.ksdme.lingo.services.QuestionOverlayService

class MainActivity : AppCompatActivity() {
    private val logTag = "MainActivity"
    private val requestCodeOverlay = 9358

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        if (safelyCheckIfHasOverlayAbility(this))
            this.initOverlayStuff()
        else if (doesRequireOverlayAbility())
            requestOverlayAbility(this, this.requestCodeOverlay)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == this.requestCodeOverlay) {
            if (safelyCheckIfHasOverlayAbility(this))
                this.initOverlayStuff()
        }
    }

    private fun initOverlayStuff() {
        made_with_love.setOnClickListener {
            Log.d(this.logTag, "launchService")
            this.launchService()
        }
    }

    private fun launchService() {
        startService(Intent(this, QuestionOverlayService::class.java))
    }
}
