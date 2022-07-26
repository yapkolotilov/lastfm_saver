package me.kolotilov.lastfm_saver.di

import android.content.Context
import androidx.room.Room
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import me.kolotilov.lastfm_saver.presentation.album_details.AlbumDetailsViewModel
import me.kolotilov.lastfm_saver.presentation.artist_albums.ArtistAlbumsViewModel
import me.kolotilov.lastfm_saver.presentation.common.ErrorFormatter
import me.kolotilov.lastfm_saver.presentation.common.ResourceProvider
import me.kolotilov.lastfm_saver.presentation.common.ResourceProviderImpl
import me.kolotilov.lastfm_saver.presentation.saved_albums.SavedAlbumsViewModel
import me.kolotilov.lastfm_saver.presentation.search_artists.SearchArtistsViewModel
import me.kolotilov.lastfm_saver.repositories.common.FileHelper
import me.kolotilov.lastfm_saver.repositories.common.FileHelperImpl
import me.kolotilov.lastfm_saver.repositories.network.ArrayOrInstanceDeserializer
import me.kolotilov.lastfm_saver.repositories.network.LastFmApi
import me.kolotilov.lastfm_saver.repositories.network.LastFmRepository
import me.kolotilov.lastfm_saver.repositories.network.LastFmRepositoryImpl
import me.kolotilov.lastfm_saver.repositories.network.dtos.AlbumInfoResponseDto.AlbumDto.TracksDto.TrackDto
import me.kolotilov.lastfm_saver.repositories.network.paging.ArtistAlbumsPagingSource
import me.kolotilov.lastfm_saver.repositories.network.paging.SearchArtistsPagingSource
import me.kolotilov.lastfm_saver.repositories.persistance.repositories.AlbumDatabase
import me.kolotilov.lastfm_saver.repositories.persistance.repositories.AlbumsDao
import me.kolotilov.lastfm_saver.repositories.persistance.repositories.LocalRepository
import me.kolotilov.lastfm_saver.repositories.persistance.repositories.LocalRepositoryImpl
import me.kolotilov.lastfm_saver.utils.Logger
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create


val mainModule = module {

    viewModel { SavedAlbumsViewModel(get(), get()) }
    viewModel { SearchArtistsViewModel(get(), get()) }
    viewModel { ArtistAlbumsViewModel(get(), get(), get()) }
    viewModel { AlbumDetailsViewModel(get(), get()) }

    single { okHttpClient() }
    single { retrofit(get()) }
    single { lastFmApi(get()) }
    single { albumsDatabase(get()) }
    single { albumsDao(get()) }
    single { ErrorFormatter(get()) }
    single<FileHelper> { FileHelperImpl(get()) }

    factory { SearchArtistsPagingSource.Provider(get()) }
    factory { ArtistAlbumsPagingSource.Provider(get()) }

    single<ResourceProvider> { ResourceProviderImpl(get()) }
    single<LastFmRepository> { LastFmRepositoryImpl(get(), get()) }
    single<LocalRepository> { LocalRepositoryImpl(get(), get()) }
}

private fun okHttpClient(): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor { chain ->
            val original = chain.request()
            val originalHttpUrl: HttpUrl = original.url()

            val url = originalHttpUrl.newBuilder()
                .addQueryParameter("api_key", "b15f2f2c63ca19ab00ca71b89178392f")
                .addQueryParameter("format", "json")
                .build()

            Logger("NETWORK").debug(url)

            val requestBuilder = original.newBuilder()
                .url(url)

            val request = requestBuilder.build()
            chain.proceed(request)
        }
        .build()
}

private fun retrofit(client: OkHttpClient): Retrofit {
    val type = object : TypeToken<List<TrackDto>>() {}.type
    val gson = GsonBuilder()
        .registerTypeAdapter(type, ArrayOrInstanceDeserializer<TrackDto>())
        .create()

    return Retrofit.Builder()
        .client(client)
        .baseUrl("https://ws.audioscrobbler.com/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
}

private fun lastFmApi(retrofit: Retrofit): LastFmApi {
    return retrofit.create()
}

private fun albumsDatabase(context: Context): AlbumDatabase {
    return Room.databaseBuilder(
        context,
        AlbumDatabase::class.java,
        "albums-database"
    ).build()
}

private fun albumsDao(database: AlbumDatabase): AlbumsDao {
    return database.albumsDao
}