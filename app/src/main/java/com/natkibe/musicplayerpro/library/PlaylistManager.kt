package com.natkibe.musicplayerpro.library

import android.content.Context
import com.natkibe.musicplayerpro.data.AudioItemEntity
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

/**
 * Simple playlist manager for creating and managing playlists.
 * Playlists are stored as JSON files in the app's internal storage.
 * Lightweight and offline-first — no database needed for playlists.
 */
class PlaylistManager(private val context: Context) {

    private val playlistsDir: File by lazy {
        File(context.filesDir, "playlists").also { it.mkdirs() }
    }

    /**
     * Get all playlist names.
     */
    fun getPlaylistNames(): List<String> {
        return playlistsDir.listFiles()
            ?.filter { it.extension == "json" }
            ?.map { it.nameWithoutExtension }
            ?.sorted() ?: emptyList()
    }

    /**
     * Get songs in a playlist.
     */
    fun getPlaylist(name: String): List<String> {
        val file = getPlaylistFile(name)
        if (!file.exists()) return emptyList()
        return try {
            val json = JSONObject(file.readText())
            val uris = json.getJSONArray("uris")
            (0 until uris.length()).map { uris.getString(it) }
        } catch (_: Exception) {
            emptyList()
        }
    }

    /**
     * Create or update a playlist with the given song URIs.
     */
    fun savePlaylist(name: String, uris: List<String>) {
        val file = getPlaylistFile(name)
        val json = JSONObject().apply {
            put("name", name)
            put("uris", JSONArray(uris))
            put("updatedAt", System.currentTimeMillis())
        }
        file.writeText(json.toString())
    }

    /**
     * Add a song to a playlist.
     */
    fun addToPlaylist(playlistName: String, audioUri: String) {
        val uris = getPlaylist(playlistName).toMutableList()
        if (audioUri !in uris) {
            uris.add(audioUri)
            savePlaylist(playlistName, uris)
        }
    }

    /**
     * Remove a song from a playlist.
     */
    fun removeFromPlaylist(playlistName: String, audioUri: String) {
        val uris = getPlaylist(playlistName).toMutableList()
        if (uris.remove(audioUri)) {
            savePlaylist(playlistName, uris)
        }
    }

    /**
     * Delete a playlist.
     */
    fun deletePlaylist(name: String) {
        getPlaylistFile(name).delete()
    }

    /**
     * Get folders grouped by storage type (USB/External vs System/Internal).
     */
    fun groupByStorage(folders: List<AudioFolder>): Map<String, List<AudioFolder>> {
        return folders.groupBy { it.storageType }
    }

    private fun getPlaylistFile(name: String): File {
        val safeName = name.replace(Regex("[^a-zA-Z0-9_\\- ]"), "_")
        return File(playlistsDir, "$safeName.json")
    }
}
