package com.natkibe.musicplayerpro.artwork

import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import java.io.File
import java.io.FileOutputStream

/**
 * Optional disk-based artwork thumbnail cache.
 * OFF by default — only used when artwork setting is enabled.
 * Stores small thumbnails to avoid re-decoding full artwork on every bind.
 */
class ArtworkCache(private val context: Context) {

    private val cacheDir: File by lazy {
        File(context.cacheDir, "artwork_thumbnails").also { it.mkdirs() }
    }

    /**
     * Get cached thumbnail for an audio URI, or null if not cached.
     */
    fun getCachedThumbnail(audioUri: String): Bitmap? {
        val file = getCacheFile(audioUri)
        return if (file.exists()) {
            BitmapFactory.decodeFile(file.absolutePath)
        } else null
    }

    /**
     * Load and cache a thumbnail from MediaStore.
     * Returns the bitmap or null if unavailable.
     */
    fun loadThumbnail(audioUri: String): Bitmap? {
        // Try cache first
        getCachedThumbnail(audioUri)?.let { return it }

        // Load from MediaStore
        val audioId = try {
            ContentUris.parseId(Uri.parse(audioUri))
        } catch (e: NumberFormatException) {
            return null
        }

        val bitmap = MediaStore.Audio.Media.getBitmap(
            context.contentResolver,
            ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, audioId)
        ) ?: return null

        // Cache it
        cacheBitmap(audioUri, bitmap)
        return bitmap
    }

    /**
     * Clear the entire artwork cache.
     */
    fun clearCache() {
        cacheDir.listFiles()?.forEach { it.delete() }
    }

    private fun getCacheFile(audioUri: String): File {
        val hash = audioUri.hashCode().toUInt().toString(16)
        return File(cacheDir, "$hash.jpg")
    }

    private fun cacheBitmap(audioUri: String, bitmap: Bitmap) {
        try {
            val file = getCacheFile(audioUri)
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 85, out)
            }
        } catch (_: Exception) {
            // Silently fail — cache miss is acceptable
        }
    }
}
