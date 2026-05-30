package com.natkibe.musicplayerpro.data
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "audio_items") data class AudioItemEntity(@PrimaryKey val uri: String, val title: String, val artist: String?, val album: String?, val folderName: String, val relativePath: String?, val storageType: String, val mimeType: String?, val durationMs: Long, val sizeBytes: Long, val dateModified: Long, val trackNumber: Int?)
