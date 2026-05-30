package com.natkibe.musicplayerpro.playback

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackParameters
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.natkibe.musicplayerpro.data.AudioItemEntity
import com.natkibe.musicplayerpro.data.AudioProgressEntity
import com.natkibe.musicplayerpro.data.ProgressDao
import com.natkibe.musicplayerpro.settings.SettingsStore

class PlayerHolder(
    context: Context,
    private val progressDao: ProgressDao,
    private val settingsStore: SettingsStore
) {
    val player: ExoPlayer = ExoPlayer.Builder(context.applicationContext).build()

    private var current: AudioItemEntity? = null
    private val speeds = listOf(1.0f, 1.25f, 1.5f, 0.75f)
    private var speedIndex = 0
    private var isShuffled = false
    private var queue: List<AudioItemEntity> = emptyList()
    private var shuffleOrder: List<Int> = emptyList()

    suspend fun play(item: AudioItemEntity) {
        current = item
        player.setMediaItem(MediaItem.fromUri(item.uri))
        player.prepare()
        val saved = progressDao.get(item.uri)
        if (saved != null && saved.positionMs > 10_000 && saved.positionMs < saved.durationMs - 10_000) {
            player.seekTo(saved.positionMs)
        }
        player.play()
    }

    fun playQueue(items: List<AudioItemEntity>, startIndex: Int = 0) {
        queue = items
        val mediaItems = items.map { MediaItem.fromUri(it.uri) }
        player.setMediaItems(mediaItems, startIndex, 0)
        player.prepare()
        player.play()
        current = items.getOrNull(startIndex)
    }

    fun toggle() {
        if (player.isPlaying) player.pause() else player.play()
    }

    fun cycleRepeat() {
        player.repeatMode = when (player.repeatMode) {
            Player.REPEAT_MODE_OFF -> Player.REPEAT_MODE_ONE
            Player.REPEAT_MODE_ONE -> Player.REPEAT_MODE_ALL
            else -> Player.REPEAT_MODE_OFF
        }
    }

    fun toggleShuffle() {
        isShuffled = !isShuffled
        player.shuffleModeEnabled = isShuffled
    }

    fun cycleSpeed() {
        speedIndex = (speedIndex + 1) % speeds.size
        player.playbackParameters = PlaybackParameters(speeds[speedIndex])
    }

    fun getCurrentSpeed(): Float = speeds[speedIndex]

    suspend fun saveProgress() {
        val item = current ?: return
        val dur = player.duration.takeIf { it > 0 } ?: item.durationMs
        progressDao.save(
            AudioProgressEntity(
                audioUri = item.uri,
                positionMs = player.currentPosition,
                durationMs = dur,
                updatedAt = System.currentTimeMillis(),
                completed = player.currentPosition > dur - 10_000
            )
        )
    }

    fun release() = player.release()
}
