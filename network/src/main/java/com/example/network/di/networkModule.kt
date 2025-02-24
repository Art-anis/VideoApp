package com.example.network.di

import com.example.network.BuildConfig
import com.example.network.api.VideoApi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//базовый url
const val baseUrl = "https://api.dyntube.com/v1/"

//provide для retrofit
fun provideRetrofit(
    baseUrl: String,
    client: OkHttpClient
): Retrofit {
    return Retrofit
        .Builder()
        .baseUrl(baseUrl) //базовый url
        .addConverterFactory(GsonConverterFactory.create()) //конвертер
        .client(client) //клиент
        .build()
}

//provide для клиента
fun provideClient(): OkHttpClient {
    val httpClient = OkHttpClient.Builder()
    //вставляем в заголовок api ключ
    httpClient.addInterceptor(Interceptor { chain ->
        val original = chain.request()

        val request = original.newBuilder()
            .header("Authorization", BuildConfig.API_KEY)
            .method(original.method, original.body)
            .build()

        chain.proceed(request)
    })
    return httpClient.build()
}

//provide для api
fun provideApi(retrofit: Retrofit): VideoApi {
    return retrofit.create(VideoApi::class.java)
}

//модуль сети
val networkModule = module {

    single { baseUrl }

    single { provideClient() }

    single { provideRetrofit(
        baseUrl = get(),
        client = get()
    ) }

    single { provideApi(retrofit = get()) }
}