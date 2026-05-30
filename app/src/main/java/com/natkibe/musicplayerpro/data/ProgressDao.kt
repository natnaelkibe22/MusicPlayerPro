package com.natkibe.musicplayerpro.data
import androidx.room.*
@Dao interface ProgressDao { @Query("SELECT * FROM audio_progress WHERE audioUri=:uri") suspend fun get(uri: String): AudioProgressEntity?; @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun save(progress: AudioProgressEntity); @Query("SELECT * FROM audio_progress ORDER BY updatedAt DESC LIMIT 20") suspend fun recent(): List<AudioProgressEntity> }
