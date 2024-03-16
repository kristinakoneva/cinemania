package mk.ukim.finki.cinemania.data.api.interceptors

import javax.inject.Inject
import mk.ukim.finki.cinemania.BuildConfig
import mk.ukim.finki.cinemania.utils.Constants.QUERY_NAME_API_KEY
import okhttp3.Interceptor
import okhttp3.Response

class MoviesApiKeyQueryInterceptor @Inject constructor() : Interceptors.MoviesApiKeyQuery {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val urlWithApiKeyQuery = request.url.newBuilder().addQueryParameter(QUERY_NAME_API_KEY, BuildConfig.MOVIES_API_KEY).build()
        request = request.newBuilder().url(urlWithApiKeyQuery).build()
        return chain.proceed(request)
    }
}