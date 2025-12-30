package com.uniumuniu.tv.remote.data

import com.uniumuniu.tv.domain.model.CommandType
import com.uniumuniu.tv.domain.model.SendCommandResult
import com.uniumuniu.tv.domain.repository.TokenRepository
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Test
import retrofit2.Response

class TvRemoteRepositoryImplTest {

    private val mockTvRemoteApi = mockk<TvRemoteApi> {
        coEvery { this@mockk.sendCommand(any(), any()) } returns Response.success(
            CommandResponse(
                status = "ACCEPTED"
            )
        )
    }
    private val mockTokenRepository = mockk<TokenRepository> {
        every { this@mockk.getToken() } returns TvRemoteRepositoryImpl.VALID_TOKEN
    }

    private val repository = TvRemoteRepositoryImpl(
        tvRemoteApi = mockTvRemoteApi,
        tokenRepository = mockTokenRepository,
    )

    @Test
    fun `when token is registered, return success`() = runTest {
        val result = repository.sendCommand(CommandType.turnOn)

        assert(result == SendCommandResult.Success)
    }

    @Test
    fun `when token is null, return missing token result`() = runTest {
        every { mockTokenRepository.getToken() } returns null

        val result = repository.sendCommand(CommandType.turnOn)

        assert(result == SendCommandResult.MissingToken)
    }

    @Test
    fun `when server returns unauthorized, return unauthorized result`() = runTest {
        val errorBody = """{ "message": "Unauthorized" }""".toResponseBody(
            "application/json".toMediaType(),
        )
        val response = Response.error<CommandResponse>(401, errorBody)
        coEvery { mockTvRemoteApi.sendCommand(any(), any()) } returns response

        val result = repository.sendCommand(CommandType.turnOn)

        assert(result == SendCommandResult.NotAuthorized)
    }

    @Test
    fun `when server returns unknown error, return unknown error result`() = runTest {
        val errorBody = """{ "message": "Unknown" }""".toResponseBody(
            "application/json".toMediaType(),
        )
        val response = Response.error<CommandResponse>(404, errorBody)
        coEvery { mockTvRemoteApi.sendCommand(any(), any()) } returns response

        val result = repository.sendCommand(CommandType.turnOn)

        assert(result == SendCommandResult.UnknownError)
    }
}