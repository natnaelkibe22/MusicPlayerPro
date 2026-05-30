package com.natkibe.musicplayerpro.library

import com.natkibe.musicplayerpro.data.AudioItemEntity

/**
 * Contract interface for the Library module.
 * Defines the public API surface for MediaStore import and Room cache operations.
 */
interface LibraryContract {
    /** Get cached folders from Room (instant, no storage scan). */
    suspend fun getCachedFolders(): List<AudioFolder>

    /** Get songs for a specific folder from Room cache. */
    suspend fun getSongs(folder: String): List<AudioItemEntity>

    /** Search cached songs by title or artist. */
    suspend fun searchSongs(query: String): List<AudioItemEntity>

    /** Refresh from MediaStore in background and update Room cache. */
    suspend fun refreshFromMediaStore()
}
