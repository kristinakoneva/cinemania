package mk.ukim.finki.cinemania.data.api.interceptors

import dagger.Module
import dagger.Provides
import dagger.hilt.migration.DisableInstallInCheck
import javax.inject.Singleton

@Module
@DisableInstallInCheck
class InterceptorsModule {

    @Provides
    @Singleton
    fun moviesApiKeyQueryInterceptor(interceptor: MoviesApiKeyQueryInterceptor): Interceptors.MoviesApiKeyQuery = interceptor
}