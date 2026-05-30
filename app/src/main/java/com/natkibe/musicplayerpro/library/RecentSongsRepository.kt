package com.natkibe.musicplayerpro.library

import com.natkibe.musicplayerpro.data.AudioDao
import com.natkibe.musicplayerpro.data.AudioItemEntity
import com.natkibe.musicplayerpro.data.AudioProgressEntity
import com.natkibe.musicplayerpro.data.ProgressDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Manages recently played songs and resume playback position.
 * Uses Room-based progress persistence for reliable state across restarts.
 */
class RecentSongsRepository(
    private val progressDao: ProgressDao,
    private val audioDao: AudioDao
) {

    /**
     * Save playback progress for a song.
     */
    suspend fun saveProgress(audioUri: String, positionMs: Long, durationMs: Long) {
        withContext(Dispatchers.IO) {
            progressDao.save(
                AudioProgressEntity(
                    audioUri = audioUri,
                    positionMs = positionMs,
                    durationMs = durationMs,
                    updatedAt = System.currentTimeMillis(),
                    completed = positionMs > durationMs - 10_000
                )
            )
        }
    }

    /**
     * Get saved progress for a specific song.
     */
    suspend fun getProgress(audioUri: String): AudioProgressEntity? = withContext(Dispatchers.IO) {
        progressDao.get(audioUri)
    }

    /**
     * Get recently played songs (up to 20).
     */
    suspend fun getRecentSongs(): List<AudioItemEntity> = withContext(Dispatchers.IO) {
        val recent = progressDao.recent()
        val uris = recent.map { it.audioUri }
        // Fetch from audio DAO to get full song details
        audioDao.getAll().filter { it.uri in uris }
            .sortedByDescending { uris.indexOf(it.uri) }
    }

    /**
     * Get the last played song for resume functionality.
     */
    suspend fun getLastPlayed(): AudioItemEntity? = withContext(Dispatchers.IO) {
        val recent = progressDao.recent()
        val last = recent.firstOrNull() ?: return@withContext null
        audioDao.getAll().find { it.uri == last.audioUri }
    }

    /**
     * Get resume position for the last played song.
     */
    suspend fun getLastPlayedProgress(): AudioProgressEntity? = withContext(Dispatchers.IO) {
        progressDao.recent().firstOrNull()?.let { progressDao.get(it.audioUri) }
    }
}
