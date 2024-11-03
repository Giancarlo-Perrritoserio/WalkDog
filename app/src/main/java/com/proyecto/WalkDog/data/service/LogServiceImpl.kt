package com.proyecto.WalkDog.data.service

import android.util.Log
import javax.inject.Inject

class LogServiceImpl @Inject constructor() : LogService {
    private val TAG = "WalkDog"

    override fun logEvent(event: String) {
        Log.d(TAG, "Event: $event")
    }

    override fun logError(error: String) {
        Log.e(TAG, "Error: $error")
    }

    override fun logInfo(info: String) {
        Log.i(TAG, "Info: $info")
    }
}
