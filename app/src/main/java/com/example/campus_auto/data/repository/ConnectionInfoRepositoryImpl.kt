package com.example.campus_auto.data.repository

import com.example.campus_auto.data.remote.IpAddressApiClient
import com.example.campus_auto.data.remote.IpAddressService

class ConnectionInfoRepositoryImpl(
    private val ipAddressService: IpAddressService
) : ConnectionInfoRepository {
    override suspend fun ipAddress(): Result<String> {
        return runCatching {
            ipAddressService.getIpAddress()
        }
    }

    companion object {
        fun default() : ConnectionInfoRepository {
            val ipAddressService = IpAddressApiClient.getApiClient().create(IpAddressService::class.java)
            return ConnectionInfoRepositoryImpl(ipAddressService)
        }
    }
}
