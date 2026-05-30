package com.natkibe.musicplayerpro.data
import android.content.Context
import androidx.room.*
@Database(entities = [AudioItemEntity::class, AudioProgressEntity::class], version = 1, exportSchema = true) abstract class AppDatabase: RoomDatabase() { abstract fun audioDao(): AudioDao; abstract fun progressDao(): ProgressDao; companion object { @Volatile private var INSTANCE: AppDatabase? = null; fun get(context: Context): AppDatabase = INSTANCE ?: synchronized(this) { INSTANCE ?: Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "music_player_pro.db").build().also { INSTANCE = it } } } }
