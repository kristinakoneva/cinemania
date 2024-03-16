package mk.ukim.finki.cinemania.di.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import mk.ukim.finki.cinemania.data.api.interceptors.InterceptorsModule
import mk.ukim.finki.cinemania.di.qualifiers.MoviesApi
import mk.ukim.finki.cinemania.utils.Constants.MOVIES_API_BASE_URL
import okhttp3.OkHttpClient

@Module(includes = [InterceptorsModule::class])
@InstallIn(SingletonComponent::class)
class HttpClientsModule {

    @Provides
    @MoviesApi
    fun moviesApiBaseUrl(): String = MOVIES_API_BASE_URL

    @Provides
    @Singleton
    @MoviesApi
    fun moviesApiOkHttpClient(): OkHttpClient = OkHttpClient.Builder().build()
}