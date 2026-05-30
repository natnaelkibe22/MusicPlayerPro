package com.natkibe.musicplayerpro.equalizer
import android.media.audiofx.Equalizer
class EqualizerController { private var eq: Equalizer?=null; fun attach(audioSessionId:Int){release(); if(audioSessionId!=0) eq=Equalizer(0,audioSessionId).apply{enabled=true}}; fun setBandLevelSafe(band:Short, level:Short){runCatching{eq?.setBandLevel(band,level)}}; fun release(){runCatching{eq?.release()}; eq=null} }
