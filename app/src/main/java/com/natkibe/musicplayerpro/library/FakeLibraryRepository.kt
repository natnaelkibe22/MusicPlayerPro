package com.natkibe.musicplayerpro.library

import com.natkibe.musicplayerpro.data.AudioItemEntity

/**
 * Fake implementation of LibraryContract for testing.
 * Returns sample data without requiring MediaStore or Room.
 */
class FakeLibraryRepository : LibraryContract {

    private val sampleFolders = listOf(
        AudioFolder("Rock", 5, "System/Internal"),
        AudioFolder("Jazz", 3, "System/Internal"),
        AudioFolder("Podcasts", 8, "USB/External"),
        AudioFolder("Classical", 4, "USB/External")
    )

    private val sampleSongs = mapOf(
        "Rock" to listOf(
            createSampleSong("Song 1", "Artist A", "Rock"),
            createSampleSong("Song 2", "Artist A", "Rock"),
            createSampleSong("Song 3", "Artist B", "Rock"),
            createSampleSong("Song 4", "Artist B", "Rock"),
            createSampleSong("Song 5", "Artist C", "Rock")
        ),
        "Jazz" to listOf(
            createSampleSong("Blue Note", "Artist D", "Jazz"),
            createSampleSong("Autumn Leaves", "Artist D", "Jazz"),
            createSampleSong("Take Five", "Artist E", "Jazz")
        ),
        "Podcasts" to (1..8).map {
            createSampleSong("Episode $it", "Podcaster", "Podcasts")
        },
        "Classical" to listOf(
            createSampleSong("Symphony No. 5", "Beethoven", "Classical"),
            createSampleSong("The Four Seasons", "Vivaldi", "Classical"),
            createSampleSong("Clair de Lune", "Debussy", "Classical"),
            createSampleSong("Canon in D", "Pachelbel", "Classical")
        )
    )

    override suspend fun getCachedFolders(): List<AudioFolder> = sampleFolders

    override suspend fun getSongs(folder: String): List<AudioItemEntity> =
        sampleSongs[folder] ?: emptyList()

    override suspend fun searchSongs(query: String): List<AudioItemEntity> =
        sampleSongs.values.flatten().filter {
            it.title.contains(query, ignoreCase = true) ||
            (it.artist?.contains(query, ignoreCase = true) == true)
        }

    override suspend fun refreshFromMediaStore() {
        // No-op for fake
    }

    private fun createSampleSong(
        title: String,
        artist: String,
        folder: String
    ): AudioItemEntity = AudioItemEntity(
        uri = "content://media/external/audio/media/fake_${title.hashCode()}",
        title = title,
        artist = artist,
        album = "$folder Album",
        folderName = folder,
        relativePath = "$folder/",
        storageType = if (folder == "Podcasts" || folder == "Classical") "USB/External"
        else "System/Internal",
        mimeType = "audio/mpeg",
        durationMs = (180_000..300_000).random().toLong(),
        sizeBytes = (5_000_000..15_000_000).random().toLong(),
        dateModified = System.currentTimeMillis() / 1000,
        trackNumber = null
    )
}
