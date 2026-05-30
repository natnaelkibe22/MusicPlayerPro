package com.natkibe.musicplayerpro.floating

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.provider.Settings
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import com.natkibe.musicplayerpro.MusicPlayerProApp
import com.natkibe.musicplayerpro.R

class FloatingControlsService : Service() {

    private var wm: WindowManager? = null
    private var view: View? = null
    private var params: WindowManager.LayoutParams? = null

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
            !Settings.canDrawOverlays(this)
        ) {
            Toast.makeText(this, "SYSTEM_ALERT_WINDOW permission required", Toast.LENGTH_LONG).show()
            stopSelf()
            return START_NOT_STICKY
        }
        show()
        return START_NOT_STICKY
    }

    private fun show() {
        if (view != null) return

        val app = application as MusicPlayerProApp
        val playerHolder = app.appContainer.playerHolder

        wm = getSystemService(WINDOW_SERVICE) as WindowManager
        view = LayoutInflater.from(this).inflate(R.layout.floating_controls, null)

        val type = if (Build.VERSION.SDK_INT >= 26) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            WindowManager.LayoutParams.TYPE_PHONE
        }

        params = WindowManager.LayoutParams(
            420,
            96,
            type,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
            x = 40
            y = 120
        }

        // Drag handling
        var sx = 0f
        var sy = 0f
        var px = 0
        var py = 0

        view!!.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    sx = event.rawX
                    sy = event.rawY
                    px = params!!.x
                    py = params!!.y
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    params!!.x = px + (event.rawX - sx).toInt()
                    params!!.y = py + (event.rawY - sy).toInt()
                    wm?.updateViewLayout(view, params)
                    true
                }
                else -> false
            }
        }

        // Wire up control buttons
        view!!.findViewById<Button>(R.id.floatingPrevButton).setOnClickListener {
            playerHolder.player.seekToPrevious()
        }

        view!!.findViewById<Button>(R.id.floatingPlayPauseButton).setOnClickListener {
            playerHolder.toggle()
        }

        view!!.findViewById<Button>(R.id.floatingNextButton).setOnClickListener {
            playerHolder.player.seekToNext()
        }

        view!!.findViewById<Button>(R.id.floatingCloseButton).setOnClickListener {
            stopSelf()
        }

        wm?.addView(view, params)
    }

    override fun onDestroy() {
        view?.let { wm?.removeView(it) }
        view = null
        super.onDestroy()
    }
}
