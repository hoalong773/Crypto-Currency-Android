package com.namnguyen.cryptocurrency.di

import com.namnguyen.cryptocurrency.BuildConfig
import com.namnguyen.cryptocurrency.data.repository.UserRepositoryImp
import com.namnguyen.cryptocurrency.data.service.ApiService
import com.namnguyen.cryptocurrency.domain.repository.UserRepository
import com.namnguyen.cryptocurrency.domain.usecase.GetListCryptoUseCase
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

private const val TIME_OUT = 30L

val NetworkModule = module {

    single { createService(get()) }

    single { createRetrofit(get(), BuildConfig.BASE_URL) }

    single { createOkHttpClient() }

    single { MoshiConverterFactory.create() }

    single { Moshi.Builder().build() }

}

fun createOkHttpClient(): OkHttpClient {
    return OkHttpClient.Builder()
        .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
        .readTimeout(TIME_OUT, TimeUnit.SECONDS)
        .addInterceptor(
            HttpLoggingInterceptor()
                .setLevel(
                    if (BuildConfig.DEBUG)
                        HttpLoggingInterceptor.Level.BODY
                    else
                        HttpLoggingInterceptor.Level.NONE
                )
        ).build()
}

fun createRetrofit(okHttpClient: OkHttpClient, url: String): Retrofit {
    return Retrofit.Builder()
        .baseUrl(url)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create()).build()
}

fun createService(retrofit: Retrofit): ApiService {
    return retrofit.create(ApiService::class.java)
}

fun createCurrencyRepository(apiService: ApiService): UserRepository {
    return UserRepositoryImp(apiService)
}

fun createGetCurrencyUseCase(userRepository: UserRepository): GetListCryptoUseCase {
    return GetListCryptoUseCase(userRepository)
}
