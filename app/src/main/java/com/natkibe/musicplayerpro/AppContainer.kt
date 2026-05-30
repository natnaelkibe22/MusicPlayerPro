package com.natkibe.musicplayerpro

import android.content.Context
import com.natkibe.musicplayerpro.artwork.ArtworkCache
import com.natkibe.musicplayerpro.artwork.ArtworkLoader
import com.natkibe.musicplayerpro.data.AppDatabase
import com.natkibe.musicplayerpro.library.AudioLibraryRepository
import com.natkibe.musicplayerpro.library.RecentSongsRepository
import com.natkibe.musicplayerpro.playback.PlayerHolder
import com.natkibe.musicplayerpro.settings.SettingsStore

class AppContainer(context: Context) {
    val database = AppDatabase.get(context)
    val settingsStore = SettingsStore(context)
    val libraryRepository = AudioLibraryRepository(context, database.audioDao())
    val recentSongsRepository = RecentSongsRepository(database.progressDao(), database.audioDao())
    val playerHolder = PlayerHolder(context, database.progressDao(), settingsStore)
    val artworkLoader = ArtworkLoader()
    val artworkCache = ArtworkCache(context)
}
