package com.natkibe.musicplayerpro.settings

/**
 * Contract interface for the Settings module.
 * Defines the public API surface for DataStore preference toggles.
 */
interface SettingsContract {
    /** Toggle artwork thumbnail display. */
    suspend fun toggleArtwork()

    /** Toggle floating controls overlay. */
    suspend fun toggleFloatingControls()

    /** Check if artwork is enabled. */
    suspend fun artworkEnabled(): Boolean

    /** Check if floating controls are enabled. */
    suspend fun floatingEnabled(): Boolean
}
