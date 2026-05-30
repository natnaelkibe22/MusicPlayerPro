# Music Player Pro — Android Headunit Micro-Module Project

Music Player Pro is a lightweight, offline-first Kotlin Android music player for weak Android headunits. It borrows the fast-library idea of Symfonium but stays folder-first, toggle-based, and car-friendly.

## Scope
- Local audio only: MP3, AAC/M4A, FLAC, WAV, OGG, OPUS, and other MediaStore-supported audio.
- Media3 ExoPlayer playback.
- MediaStore import, then Room cache for instant startup.
- DataStore for settings.
- Optional artwork thumbnails, OFF by default.
- Optional transparent floating controls, OFF by default.
- Lightweight equalizer module, optional.
- Resume memory, recent songs, folders, playlists/queue-ready architecture.

## Micro-Modules
- `library/` — MediaStore audio import and cached folders/songs.
- `data/` — Room entities and DAOs.
- `playback/` — Media3 ExoPlayer holder and service placeholder.
- `player/` — Now Playing UI and queue/folder side list.
- `settings/` — DataStore toggles.
- `floating/` — transparent mini controls overlay.
- `equalizer/` — optional Android Equalizer wrapper.
- `artwork/` — optional artwork loader contract.

## Performance Rules
1. Never recursively scan storage on launch.
2. Show cached Room data first; refresh MediaStore in background.
3. Lazy-load album art only when visible and only when setting is enabled.
4. Use one shared ExoPlayer holder/service.
5. Keep floating controls simple: no animations, no heavy rendering.
6. Feature exists does not mean feature always runs. Everything optional starts OFF.

## Readiness
This is a v0.9 architecture/contracts-ready project. It contains a near-complete project skeleton, milestones, prompts, Maestro tests, and feature-contained modules. Open in Android Studio, Gradle sync, then fix any device-specific compile/runtime issues.
