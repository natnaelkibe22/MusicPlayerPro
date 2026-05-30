package com.natkibe.musicplayerpro.player

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.natkibe.musicplayerpro.MusicPlayerProApp
import com.natkibe.musicplayerpro.R
import com.natkibe.musicplayerpro.data.AudioItemEntity
import com.natkibe.musicplayerpro.floating.FloatingControlsService
import kotlinx.coroutines.launch

class NowPlayingActivity : AppCompatActivity() {

    private val container by lazy { (application as MusicPlayerProApp).appContainer }
    private lateinit var adapter: SongAdapter
    private var currentFolder: String = "Music"
    private var songs: List<AudioItemEntity> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_now_playing)

        currentFolder = intent.getStringExtra("folder") ?: "Music"

        adapter = SongAdapter { item ->
            val index = songs.indexOf(item)
            if (index >= 0) {
                container.playerHolder.playQueue(songs, index)
                findViewById<TextView>(R.id.nowPlayingTitle).text = item.title
            }
        }

        findViewById<RecyclerView>(R.id.songList).layoutManager = LinearLayoutManager(this)
        findViewById<RecyclerView>(R.id.songList).adapter = adapter

        findViewById<Button>(R.id.playPauseButton).setOnClickListener {
            container.playerHolder.toggle()
        }

        findViewById<Button>(R.id.nextButton).setOnClickListener {
            container.playerHolder.player.seekToNext()
        }

        findViewById<Button>(R.id.prevButton).setOnClickListener {
            container.playerHolder.player.seekToPrevious()
        }

        findViewById<Button>(R.id.repeatButton).setOnClickListener {
            container.playerHolder.cycleRepeat()
            val mode = when (container.playerHolder.player.repeatMode) {
                androidx.media3.common.Player.REPEAT_MODE_ONE -> "Repeat One"
                androidx.media3.common.Player.REPEAT_MODE_ALL -> "Repeat All"
                else -> "Repeat Off"
            }
            findViewById<TextView>(R.id.nowPlayingTitle).text = mode
        }

        findViewById<Button>(R.id.speedButton).setOnClickListener {
            container.playerHolder.cycleSpeed()
            val speed = container.playerHolder.getCurrentSpeed()
            findViewById<TextView>(R.id.nowPlayingTitle).text = "Speed: ${speed}x"
        }

        findViewById<Button>(R.id.floatingButton).setOnClickListener {
            startService(Intent(this, FloatingControlsService::class.java))
        }

        // Load songs
        lifecycleScope.launch {
            songs = container.libraryRepository.getSongs(currentFolder)
            adapter.submit(songs)
            if (songs.isNotEmpty()) {
                findViewById<TextView>(R.id.nowPlayingTitle).text =
                    "Tap a song to play"
            } else {
                findViewById<TextView>(R.id.nowPlayingTitle).text =
                    "No songs found in this folder"
            }
        }
    }

    override fun onPause() {
        super.onPause()
        lifecycleScope.launch {
            container.playerHolder.saveProgress()
        }
    }
}
