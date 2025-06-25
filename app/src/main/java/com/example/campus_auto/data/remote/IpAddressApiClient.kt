package com.example.campus_auto.data.remote

import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

object IpAddressApiClient {
    private const val BASE_URL = "https://api64.ipify.org"
    fun getApiClient(): Retrofit =
        Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
}
