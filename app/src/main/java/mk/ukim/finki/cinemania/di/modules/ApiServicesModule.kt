package mk.ukim.finki.cinemania.di.modules

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import mk.ukim.finki.cinemania.data.api.MoviesApiService
import mk.ukim.finki.cinemania.di.qualifiers.MoviesApi
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit

private val json = Json { ignoreUnknownKeys = true }

@Module
@InstallIn(SingletonComponent::class)
class ApiServicesModule {

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