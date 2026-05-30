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
import com.natkibe.musicplayerpro.floating.FloatingControlsService
import kotlinx.coroutines.launch

class NowPlayingActivity : AppCompatActivity() {

    private val container by lazy { (application as MusicPlayerProApp).appContainer }
    private lateinit var adapter: SongAdapter
    private var index = 0
    private var currentFolder: String = "Music"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_now_playing)

        currentFolder = intent.getStringExtra("folder") ?: "Music"

        adapter = SongAdapter { item ->
            lifecycleScope.launch {
                container.playerHolder.play(item)
                findViewById<TextView>(R.id.nowPlayingTitle).text = item.title
            }
        }

        findViewById<RecyclerView>(R.id.songList).layoutManager = LinearLayoutManager(this)
        findViewById<RecyclerView>(R.id.songList).adapter = adapter

        findViewById<Button>(R.id.playPauseButton).setOnClickListener {
            container.playerHolder.toggle()
        }

        findViewById<Button>(R.id.nextButton).setOnClickListener {
            lifecycleScope.launch {
                val rows = container.libraryRepository.getSongs(currentFolder)
                if (rows.isNotEmpty()) {
                    index = (index + 1) % rows.size
                    container.playerHolder.play(rows[index])
                    findViewById<TextView>(R.id.nowPlayingTitle).text = rows[index].title
                }
            }
        }

        findViewById<Button>(R.id.prevButton).setOnClickListener {
            lifecycleScope.launch {
                val rows = container.libraryRepository.getSongs(currentFolder)
                if (rows.isNotEmpty()) {
                    index = (index - 1 + rows.size) % rows.size
                    container.playerHolder.play(rows[index])
                    findViewById<TextView>(R.id.nowPlayingTitle).text = rows[index].title
                }
            }
        }

        findViewById<Button>(R.id.repeatButton).setOnClickListener {
            container.playerHolder.cycleRepeat()
        }

        findViewById<Button>(R.id.speedButton).setOnClickListener {
            container.playerHolder.cycleSpeed()
        }

        findViewById<Button>(R.id.floatingButton).setOnClickListener {
            startService(Intent(this, FloatingControlsService::class.java))
        }

        // Load songs but don't auto-play
        lifecycleScope.launch {
            val rows = container.libraryRepository.getSongs(currentFolder)
            adapter.submit(rows)
            if (rows.isNotEmpty()) {
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
