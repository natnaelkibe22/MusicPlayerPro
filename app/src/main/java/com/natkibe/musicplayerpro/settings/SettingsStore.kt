package com.natkibe.musicplayerpro.settings
import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
private val Context.dataStore by preferencesDataStore("music_player_pro_settings")
class SettingsStore(private val context: Context){ private val showArtwork=booleanPreferencesKey("show_artwork"); private val floating=booleanPreferencesKey("floating_controls"); suspend fun toggleArtwork(){context.dataStore.edit{it[showArtwork]=!(it[showArtwork]?:false)}}; suspend fun toggleFloatingControls(){context.dataStore.edit{it[floating]=!(it[floating]?:false)}}; suspend fun artworkEnabled():Boolean=context.dataStore.data.first()[showArtwork]?:false; suspend fun floatingEnabled():Boolean=context.dataStore.data.first()[floating]?:false }
