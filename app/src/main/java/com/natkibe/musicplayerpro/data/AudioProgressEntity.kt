package com.natkibe.musicplayerpro.data
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "audio_progress") data class AudioProgressEntity(@PrimaryKey val audioUri: String, val positionMs: Long, val durationMs: Long, val updatedAt: Long, val completed: Boolean)
