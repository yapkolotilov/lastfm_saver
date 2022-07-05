package me.kolotilov.lastfm_saver.common

import android.app.Application
import android.content.Context

import androidx.test.runner.AndroidJUnitRunner

class MockAppRule : AndroidJUnitRunner() {

    override fun newApplication(
        cl: ClassLoader?,
        className: String?,
        context: Context?
    ): Application {
        return super.newApplication(cl, MockApp::class.java.name, context)
    }
}