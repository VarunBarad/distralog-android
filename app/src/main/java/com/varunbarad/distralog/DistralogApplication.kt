package com.varunbarad.distralog

import android.app.Application
import com.varunbarad.distralog.util.Dependencies

class DistralogApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Initialize database
        Dependencies.getDistralogDatabase(this)
    }
}
