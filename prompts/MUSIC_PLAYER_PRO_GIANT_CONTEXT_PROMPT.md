# Giant Context Prompt — Music Player Pro

Build `Music Player Pro`, package `com.natkibe.musicplayerpro`, a Kotlin Android app for car Android headunits.

Use micro-module architecture. Keep it lightweight and offline-first. It should feel like a small, fast, headunit-focused alternative to Symfonium, but not bloated.

Core stack: Kotlin, XML layouts, RecyclerView, Media3 ExoPlayer, MediaStore Audio, Room, DataStore, WorkManager, Maestro.

Required modules: library, data, playback, player, settings, floating controls, equalizer, artwork cache, recent/resume, milestones.

Important: optional features must be OFF by default to avoid lag. Artwork OFF, floating OFF, equalizer OFF, animations OFF.

Features: folder-based library, songs by folder, MediaStore import and Room cache, resume current song and recently played, play/pause/next/back/repeat/shuffle/speed, transparent floating controls overlay, optional lightweight equalizer, optional artwork thumbnails, storage tabs internal/USB, large car-friendly UI, Maestro tests by module and milestone.

Avoid streaming, browser, ads, analytics, social features, or online accounts.
