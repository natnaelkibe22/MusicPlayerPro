package com.natkibe.musicplayerpro.data
import androidx.room.*
@Dao interface AudioDao { @Query("SELECT * FROM audio_items ORDER BY folderName, title") suspend fun getAll(): List<AudioItemEntity>; @Query("SELECT * FROM audio_items WHERE folderName=:folder ORDER BY trackNumber, title") suspend fun getByFolder(folder: String): List<AudioItemEntity>; @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertAll(items: List<AudioItemEntity>); @Query("DELETE FROM audio_items") suspend fun clear() }
