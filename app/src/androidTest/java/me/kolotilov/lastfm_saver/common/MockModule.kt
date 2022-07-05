package me.kolotilov.lastfm_saver.common

import me.kolotilov.lastfm_saver.di.mainModule
import me.kolotilov.lastfm_saver.repositories.Repository
import org.koin.dsl.module

val mockModule = module {
    includes(mainModule)
    single<Repository> { MockRepositoryImpl() }
}