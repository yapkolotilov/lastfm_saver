package me.kolotilov.lastfm_saver.common

import me.kolotilov.lastfm_saver.App
import org.koin.core.module.Module

class MockApp : App() {

    override fun repositoryModule(): Module = mockModule
}