package mk.ukim.finki.cinemania.data.api.interceptors

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import okhttp3.Interceptor
import okhttp3.Response

class ChuckerInterceptor @Inject constructor(
    @ApplicationContext context: Context
) : Interceptors.Chucker {

    private val chucker = ChuckerInterceptor.Builder(context = context).build()

    override fun intercept(chain: Interceptor.Chain): Response = chucker.intercept(chain)
}