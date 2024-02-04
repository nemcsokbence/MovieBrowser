package com.mbh.moviebrowser.di

import com.mbh.moviebrowser.BuildConfig
import com.mbh.moviebrowser.api.TMDBApi
import com.mbh.moviebrowser.util.API_KEY
import com.mbh.moviebrowser.util.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    @Provides
    @Singleton
    fun provideMoviesApi(): TMDBApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder().apply {
                addInterceptor(HttpLoggingInterceptor())
                addInterceptor(Interceptor {
                    val urlBuilder = it.request().url.newBuilder()
                    val url = urlBuilder.addQueryParameter(
                        API_KEY, BuildConfig.API_KEY
                    ).build()
                    return@Interceptor it.proceed(it.request().newBuilder().url(url).build())
                })
            }.build())
            .build()
            .create(TMDBApi::class.java)
    }
}