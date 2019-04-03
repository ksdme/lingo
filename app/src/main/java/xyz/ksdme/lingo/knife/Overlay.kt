package xyz.ksdme.lingo.knife

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager

fun doesRequireOverlayAbility(): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
}

@RequiresApi(Build.VERSION_CODES.M)
fun hasOverlayAbility(context: Context): Boolean {
    return Settings.canDrawOverlays(context)
}

@RequiresApi(Build.VERSION_CODES.M)
fun requestOverlayAbility(activity: AppCompatActivity, requestCode: Int) {
    val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
        Uri.parse("package:${activity.packageName}"))

    activity.startActivityForResult(intent, requestCode)
}

fun safelyCheckIfHasOverlayAbility(context: Context): Boolean {
    if (doesRequireOverlayAbility())
        return hasOverlayAbility(context)

    return true
}

fun getOverlayType(): Int {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        return WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
    }

    return WindowManager.LayoutParams.TYPE_PHONE
}
