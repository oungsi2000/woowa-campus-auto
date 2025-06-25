package com.example.campus_auto.data.remote

import retrofit2.http.GET

interface IpAddressService {
    @GET("/")
    suspend fun getIpAddress(): String
}
