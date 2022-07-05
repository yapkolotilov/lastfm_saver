package me.kolotilov.lastfm_saver.di

import me.kolotilov.lastfm_saver.repositories.Repository
import me.kolotilov.lastfm_saver.repositories.RepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    includes(mainModule)

    single<Repository> { RepositoryImpl(get(), get()) }
}