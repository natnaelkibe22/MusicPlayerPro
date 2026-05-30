package com.natkibe.musicplayerpro.artwork

import android.content.ContentUris
import android.net.Uri
import android.provider.MediaStore
import android.widget.ImageView
import coil.load
import coil.request.ImageRequest

/**
 * Loads album artwork using Coil.
 * Artwork is OFF by default — pass enabled=true to load.
 */
class ArtworkLoader {

    companion object {
        private const val ARTWORK_ENABLED = false // default OFF
    }

    /**
     * Load artwork into the given ImageView if enabled.
     * @param enabled whether artwork loading is enabled (from settings)
     * @param imageView the target ImageView
     * @param audioUri the content URI of the audio item
     */
    fun loadIfEnabled(enabled: Boolean, imageView: ImageView, audioUri: String?) {
        if (!enabled || audioUri == null) {
            imageView.setImageDrawable(null)
            return
        }

        // Extract the audio ID from the content URI to build the album art URI
        val audioId = try {
            ContentUris.parseId(Uri.parse(audioUri))
        } catch (e: NumberFormatException) {
            null
        }

        if (audioId != null) {
            val artworkUri = ContentUris.withAppendedId(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                audioId
            )
            imageView.load(artworkUri) {
                crossfade(true)
                // Fallback: clear the image if loading fails
                listener(
                    onError = { _, _ -> imageView.setImageDrawable(null) }
                )
            }
        }
    }
}
