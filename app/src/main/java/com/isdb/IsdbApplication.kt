package com.isdb

import android.app.Application
import timber.log.Timber
import timber.log.Timber.DebugTree

class IsdbApplication : Application() {

  override fun onCreate() {
    super.onCreate()
    if (BuildConfig.DEBUG) {
      Timber.plant(DebugTree())
    }
  }
}