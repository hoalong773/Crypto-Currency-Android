package com.namnguyen.cryptocurrency

import android.app.Application
import androidx.multidex.MultiDex
import coil.ImageLoader
import coil.ImageLoaderFactory
import com.namnguyen.cryptocurrency.di.AppModule
import com.namnguyen.cryptocurrency.di.NetworkModule
import okhttp3.Cache
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import java.io.File

class MainApplication : Application(), ImageLoaderFactory {

    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)

        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@MainApplication)
            modules(listOf(AppModule, NetworkModule))
        }

    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .availableMemoryPercentage(0.25) // Use 25% of the application's available memory.
            .crossfade(true) // Show a short crossfade when loading images from network or disk.
            .componentRegistry {}
            .okHttpClient {
                // Create a disk cache with "unlimited" size. Don't do this in production.
                // To create the an optimized Coil disk cache, use CoilUtils.createDefaultCache(context).
                val cacheDirectory = File(filesDir, "image_cache").apply { mkdirs() }
                val cache = Cache(cacheDirectory, Long.MAX_VALUE)

                // Rewrite the Cache-Control header to cache all responses for a year.
                val cacheControlInterceptor =
                    ResponseHeaderInterceptor("Cache-Control", "max-age=31536000,public")

                // Don't limit concurrent network requests by host.
                val dispatcher = Dispatcher().apply { maxRequestsPerHost = maxRequests }

                // Lazily create the OkHttpClient that is used for network operations.
                OkHttpClient.Builder()
                    .cache(cache)
                    .dispatcher(dispatcher)
                    .addNetworkInterceptor(cacheControlInterceptor)
                    .build()
            }
            .apply {
                // Enable logging to the standard Android log if this is a debug build.
                /*if (BuildConfig.DEBUG) {
                    logger(DebugLogger(Log.VERBOSE))
                }*/
            }
            .build()
    }
}