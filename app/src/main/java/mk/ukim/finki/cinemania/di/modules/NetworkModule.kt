package mk.ukim.finki.cinemania.di.modules

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.serialization.json.Json
import mk.ukim.finki.cinemania.data.api.MoviesApiService
import mk.ukim.finki.cinemania.data.api.interceptors.Interceptors
import mk.ukim.finki.cinemania.data.api.interceptors.InterceptorsModule
import mk.ukim.finki.cinemania.di.qualifiers.MoviesApi
import mk.ukim.finki.cinemania.utils.Constants
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit

private val json = Json { ignoreUnknownKeys = true }

@Module(includes = [InterceptorsModule::class])
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @MoviesApi
    fun moviesApiBaseUrl(): String = Constants.MOVIES_API_BASE_URL

    @Provides
    @Singleton
    @MoviesApi
    fun moviesApiOkHttpClient(
        apiKeyQueryInterceptor: Interceptors.MoviesApiKeyQuery
    ): OkHttpClient = OkHttpClient.Builder().apply {
        addInterceptor(apiKeyQueryInterceptor)
    }.build()

    @Provides
    fun moviesApiService(@MoviesApi retrofit: Retrofit): MoviesApiService =
        retrofit.create(MoviesApiService::class.java)

    @Provides
    @MoviesApi
    fun moviesApiApiRetrofit(
        @MoviesApi baseUrl: String,
        @MoviesApi okHttpClient: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .client(okHttpClient)
        .baseUrl(baseUrl)
        .build()
}