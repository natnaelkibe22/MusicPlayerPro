package com.natkibe.musicplayerpro

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.natkibe.musicplayerpro.library.FolderAdapter
import com.natkibe.musicplayerpro.player.NowPlayingActivity
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val container by lazy { (application as MusicPlayerProApp).appContainer }
    private lateinit var adapter: FolderAdapter
    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { refresh() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = FolderAdapter { folder ->
            startActivity(
                Intent(this, NowPlayingActivity::class.java)
                    .putExtra("folder", folder.name)
            )
        }

        findViewById<RecyclerView>(R.id.folderList).layoutManager = LinearLayoutManager(this)
        findViewById<RecyclerView>(R.id.folderList).adapter = adapter

        findViewById<Button>(R.id.refreshButton).setOnClickListener { refresh() }
        findViewById<Button>(R.id.settingsButton).setOnClickListener {
            findViewById<View>(R.id.settingsPanel).visibility = View.VISIBLE
        }
        findViewById<Button>(R.id.closeSettingsButton).setOnClickListener {
            findViewById<View>(R.id.settingsPanel).visibility = View.GONE
        }
        findViewById<Button>(R.id.toggleArtworkButton).setOnClickListener {
            lifecycleScope.launch { container.settingsStore.toggleArtwork() }
        }
        findViewById<Button>(R.id.toggleFloatingButton).setOnClickListener {
            lifecycleScope.launch { container.settingsStore.toggleFloatingControls() }
        }

        requestPermissionOrLoad()
    }

    private fun requestPermissionOrLoad() {
        val p = if (Build.VERSION.SDK_INT >= 33) {
            Manifest.permission.READ_MEDIA_AUDIO
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
        if (ContextCompat.checkSelfPermission(this, p) == PackageManager.PERMISSION_GRANTED) {
            refresh()
        } else {
            permissionLauncher.launch(p)
        }
    }

    private fun refresh() {
        lifecycleScope.launch {
            findViewById<TextView>(R.id.statusText).text = "Loading cached music..."
            adapter.submit(container.libraryRepository.getCachedFolders())
            findViewById<TextView>(R.id.statusText).text = "Refreshing MediaStore..."
            container.libraryRepository.refreshFromMediaStore()
            adapter.submit(container.libraryRepository.getCachedFolders())
            findViewById<TextView>(R.id.statusText).text = "Ready"
        }
    }
}
