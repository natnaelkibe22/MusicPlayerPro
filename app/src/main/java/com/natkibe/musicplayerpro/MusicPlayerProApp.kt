package com.natkibe.musicplayerpro
import android.app.Application
class MusicPlayerProApp : Application() { val appContainer: AppContainer by lazy { AppContainer(this) } }
