package com.sg.base.di

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.sg.data.BuildConfig
import com.sg.data.api.ApiService
import com.sg.domain.util.PrefUtil
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val remoteModule = module {

    single {
        createService<ApiService>(get())
    }

    single {
        createOkHttpClient(get())
    }
}

fun createOkHttpClient(prefUtil: PrefUtil): OkHttpClient {
    val httpLoggingInterceptor = HttpLoggingInterceptor()
    httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
    return OkHttpClient.Builder()
        .readTimeout(10, TimeUnit.SECONDS)
        .connectTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
//        .addInterceptor(NoInternetInterceptor(MainApplication.instance))
        .addNetworkInterceptor(object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                var request = chain.request()
                val builder = request.newBuilder()
                val token = prefUtil.user?.token?.token
                if (token != null) {
                    builder.header("X-Http-Token", "$token")
                }
                request = builder.build()
                return chain.proceed(request)
            }
        })
        .addInterceptor(httpLoggingInterceptor)
        .addNetworkInterceptor(StethoInterceptor())
        .build()
}


inline fun <reified T> createService(okHttpClient: OkHttpClient): T {
    val retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.SERVER_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    return retrofit.create(T::class.java)
}