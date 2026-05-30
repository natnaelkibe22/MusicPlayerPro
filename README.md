# Music Player Pro — Android Headunit Micro-Module Project

Music Player Pro is a lightweight, offline-first Kotlin Android music player for weak Android headunits. It borrows the fast-library idea of Symfonium but stays folder-first, toggle-based, and car-friendly.

## Status

**v1.0 — Integration Complete**

All 10 milestones have been implemented and pushed to `main`. The project is ready for Android Studio import, Gradle sync, and device testing.

## Scope
- Local audio only: MP3, AAC/M4A, FLAC, WAV, OGG, OPUS, and other MediaStore-supported audio.
- Media3 ExoPlayer playback with queue, shuffle, repeat, and speed control.
- MediaStore import, then Room cache for instant startup.
- DataStore for settings (artwork, floating controls toggles).
- Optional artwork thumbnails via Coil, OFF by default.
- Optional transparent floating controls overlay, OFF by default.
- Lightweight equalizer module with 7 presets, optional.
- Resume memory, recent songs, folders, playlists/queue-ready architecture.
- USB/External storage detection and grouping.
- Contract interfaces and fake implementations for testing.

## Micro-Modules
- `library/` — MediaStore audio import and cached folders/songs.
- `data/` — Room entities and DAOs.
- `playback/` — Media3 ExoPlayer holder and MediaSessionService.
- `player/` — Now Playing UI and queue/folder side list.
- `settings/` — DataStore toggles.
- `floating/` — transparent mini controls overlay.
- `equalizer/` — optional Android Equalizer wrapper with presets.
- `artwork/` — optional artwork loader and disk cache.

## Performance Rules
1. Never recursively scan storage on launch.
2. Show cached Room data first; refresh MediaStore in background.
3. Lazy-load album art only when visible and only when setting is enabled.
4. Use one shared ExoPlayer holder/service.
5. Keep floating controls simple: no animations, no heavy rendering.
6. Feature exists does not mean feature always runs. Everything optional starts OFF.

## Milestones

| Milestone | Description | Status |
|-----------|-------------|--------|
| v0.1 | App shell, permissions, folder UI, dark car-friendly layout | ✅ |
| v0.2 | MediaStore audio import into Room, cached folders first | ✅ |
| v0.3 | Media3 ExoPlayer holder, play/pause/next/back/repeat/speed | ✅ |
| v0.4 | Resume playback, recent songs, position persistence | ✅ |
| v0.5 | Overlay controls, draggable, non-invasive, OFF by default | ✅ |
| v0.6 | Optional album art thumbnail cache, lazy loaded only | ✅ |
| v0.7 | Android Equalizer wrapper and simple presets, OFF by default | ✅ |
| v0.8 | USB storage grouping, folder favorites, simple playlists/queue | ✅ |
| v0.9 | Feature contracts, fakes, integration checklist, smoke tests | ✅ |
| v1.0 | Compile fixes, UI polish, headunit testing, release checklist | ✅ |

## Getting Started

1. Open in Android Studio.
2. Let Gradle sync (requires Android SDK 35).
3. Create `local.properties` with your SDK path:
   ```
   sdk.dir=/Users/youruser/Library/Android/sdk
   ```
4. Install fixture MP3/FLAC files on device/emulator.
5. Run app and grant audio permission.
6. Tap "Refresh Library" to import from MediaStore.
7. Tap a folder to browse songs, tap a song to play.

## Build

```bash
./gradlew assembleDebug
```

## Test

Maestro tests are in `maestro/` directory. Run with:
```bash
maestro test maestro/library_folders.yml
```

## Architecture

Each feature is a contained micro-module with a contract interface and implementation. See `docs/MICRO_MODULE_ARCHITECTURE.md` for details.

## License

MIT
