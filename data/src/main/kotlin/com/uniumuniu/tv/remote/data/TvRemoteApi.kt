package com.uniumuniu.tv.remote.data

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface TvRemoteApi {
    @POST("/command")
    suspend fun sendCommand(
        @Header("Authorization") authorization: String,
        @Body request: CommandRequest
    ) : Response<CommandResponse>
}