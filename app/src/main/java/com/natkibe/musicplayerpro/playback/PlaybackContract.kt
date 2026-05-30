package com.natkibe.musicplayerpro.playback

import com.natkibe.musicplayerpro.data.AudioItemEntity

/**
 * Contract interface for the Playback module.
 * Defines the public API surface for Media3 ExoPlayer operations.
 */
interface PlaybackContract {
    /** Play a single audio item. */
    suspend fun play(item: AudioItemEntity)

    /** Play a queue of items starting at the given index. */
    fun playQueue(items: List<AudioItemEntity>, startIndex: Int = 0)

    /** Toggle play/pause. */
    fun toggle()

    /** Cycle through repeat modes: OFF -> ONE -> ALL -> OFF. */
    fun cycleRepeat()

    /** Toggle shuffle mode. */
    fun toggleShuffle()

    /** Cycle through playback speeds. */
    fun cycleSpeed()

    /** Get current playback speed. */
    fun getCurrentSpeed(): Float

    /** Save current playback progress to Room. */
    suspend fun saveProgress()

    /** Release the player and free resources. */
    fun release()
}
