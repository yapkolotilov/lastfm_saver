package me.kolotilov.lastfm_saver

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import me.kolotilov.lastfm_saver.di.mainModule
import me.kolotilov.lastfm_saver.di.repositoryModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.module.Module

open class App : Application() {

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        startKoin {
            allowOverride(true)
            androidContext(this@App)
            modules(mainModule, repositoryModule())
        }
    }

    open fun repositoryModule() : Module = repositoryModule
}