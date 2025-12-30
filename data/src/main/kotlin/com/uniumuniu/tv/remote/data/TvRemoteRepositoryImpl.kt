package com.uniumuniu.tv.remote.data

import com.uniumuniu.tv.domain.model.CommandType
import com.uniumuniu.tv.domain.model.SendCommandResult
import com.uniumuniu.tv.domain.repository.TokenRepository
import com.uniumuniu.tv.domain.repository.TvRemoteRepository
import retrofit2.Response
import javax.inject.Inject

class TvRemoteRepositoryImpl @Inject constructor(
    private val tvRemoteApi: TvRemoteApi,
    private val tokenRepository: TokenRepository,
) : TvRemoteRepository {

    companion object {
        const val VALID_TOKEN = "c35bcef7-7354-48ca-afcc-5a8b0f7d1a3e"
        const val INVALID_TOKEN = "7a333900-bf41-4d15-8c6d-4c3330b7251d"
    }

    override suspend fun sendCommand(command: CommandType): SendCommandResult {
        val token = tokenRepository.getToken() ?: return SendCommandResult.MissingToken

        val response = tvRemoteApi.sendCommand(
            authorization = "Bearer $token",
            request = CommandRequest(
                type = command,
            )
        )

        return if (response.isSuccessful) {
            SendCommandResult.Success
        } else {
            mapServerError(response)
        }
    }

    private fun mapServerError(response: Response<*>): SendCommandResult {
        return when (response.code()) {
            401 -> SendCommandResult.NotAuthorized
            else -> SendCommandResult.UnknownError
        }
    }
}