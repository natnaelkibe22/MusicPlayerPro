package com.natkibe.musicplayerpro.equalizer

import android.media.audiofx.Equalizer

/**
 * Lightweight Android Equalizer wrapper.
 * OFF by default — only attach when user enables it.
 * Provides simple presets and safe band level control.
 */
class EqualizerController {

    private var eq: Equalizer? = null
    private var isEnabled = false

    /**
     * Preset names for UI display.
     */
    enum class Preset(val label: String) {
        NORMAL("Normal"),
        POP("Pop"),
        ROCK("Rock"),
        JAZZ("Jazz"),
        CLASSICAL("Classical"),
        BASS_BOOST("Bass Boost"),
        VOCAL("Vocal")
    }

    private var currentPreset = Preset.NORMAL

    /**
     * Attach equalizer to an audio session.
     * @param audioSessionId the audio session ID from ExoPlayer (0 = default)
     */
    fun attach(audioSessionId: Int) {
        release()
        if (audioSessionId != 0) {
            try {
                eq = Equalizer(0, audioSessionId).apply {
                    enabled = true
                }
                isEnabled = true
                applyPreset(currentPreset)
            } catch (_: Exception) {
                // Equalizer not available on this device
                eq = null
                isEnabled = false
            }
        }
    }

    /**
     * Set a single band level safely.
     */
    fun setBandLevelSafe(band: Short, level: Short) {
        runCatching { eq?.setBandLevel(band, level) }
    }

    /**
     * Apply a preset configuration.
     */
    fun applyPreset(preset: Preset) {
        currentPreset = preset
        val eq = eq ?: return
        if (!eq.enabled) return

        try {
            val bands = eq.numberOfBands
            if (bands < 5) return

            when (preset) {
                Preset.NORMAL -> {
                    // Flat response
                    for (i in 0 until bands) eq.setBandLevel(i.toShort(), 0)
                }
                Preset.POP -> {
                    val levels = shortArrayOf(200, 400, 0, -200, 100, 300, 400)
                    applyLevels(eq, levels)
                }
                Preset.ROCK -> {
                    val levels = shortArrayOf(400, 200, -100, -200, 100, 400, 500)
                    applyLevels(eq, levels)
                }
                Preset.JAZZ -> {
                    val levels = shortArrayOf(300, 200, -100, -100, 200, 300, 400)
                    applyLevels(eq, levels)
                }
                Preset.CLASSICAL -> {
                    val levels = shortArrayOf(400, 300, 0, -100, -100, 200, 400)
                    applyLevels(eq, levels)
                }
                Preset.BASS_BOOST -> {
                    val levels = shortArrayOf(600, 500, 200, -100, -200, -100, 0)
                    applyLevels(eq, levels)
                }
                Preset.VOCAL -> {
                    val levels = shortArrayOf(-100, 100, 400, 500, 400, 200, 0)
                    applyLevels(eq, levels)
                }
            }
        } catch (_: Exception) {
            // Ignore errors applying presets
        }
    }

    /**
     * Get the current preset.
     */
    fun getCurrentPreset(): Preset = currentPreset

    /**
     * Cycle to the next preset.
     */
    fun cyclePreset(): Preset {
        val presets = Preset.entries
        val nextIndex = (currentPreset.ordinal + 1) % presets.size
        currentPreset = presets[nextIndex]
        applyPreset(currentPreset)
        return currentPreset
    }

    /**
     * Check if equalizer is enabled and available.
     */
    fun isActive(): Boolean = isEnabled && eq != null

    /**
     * Release the equalizer resources.
     */
    fun release() {
        runCatching { eq?.release() }
        eq = null
        isEnabled = false
    }

    private fun applyLevels(eq: Equalizer, levels: ShortArray) {
        val bands = eq.numberOfBands
        for (i in 0 until minOf(bands, levels.size)) {
            eq.setBandLevel(i.toShort(), levels[i])
        }
    }
}
