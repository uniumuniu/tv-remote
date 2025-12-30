package com.uniumuniu.tv.remote.presentation

import com.uniumuniu.tv.domain.model.CommandType
import com.uniumuniu.tv.domain.model.SendCommandResult
import com.uniumuniu.tv.domain.use_case.GetTokenUseCase
import com.uniumuniu.tv.domain.use_case.RegisterTokenUseCase
import com.uniumuniu.tv.domain.use_case.SendCommandUseCase
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TvRemoteViewModelTest {

    private val mockSendCommandUseCase = mockk<SendCommandUseCase>()
    private val mockRegisterTokenUseCase = mockk<RegisterTokenUseCase>()
    private val mockGetTokenUseCase = mockk<GetTokenUseCase>()
    private val testScheduler = TestCoroutineScheduler()
    private val testDispatcher = StandardTestDispatcher(
        scheduler = testScheduler,
    )
    private lateinit var viewModel: TvRemoteViewModel

    @Test
    fun `when token is valid, set ready state`() = runTest(testDispatcher) {
        every { mockGetTokenUseCase.invoke() } returns "VALID_TOKEN"
        initViewModel()

        assert(viewModel.uiState.value == TvRemoteUiState.Loading)
        advanceUntilIdle()
        assert(viewModel.uiState.value == TvRemoteUiState.Ready)
    }

    @Test
    fun `when token is missing, set error state`() = runTest(testDispatcher) {
        every { mockGetTokenUseCase.invoke() } returns null
        initViewModel()

        assert(viewModel.uiState.value == TvRemoteUiState.Loading)
        advanceUntilIdle()
        viewModel.uiState.value.let {
            assert(it is TvRemoteUiState.Error)
            assert((it as TvRemoteUiState.Error).type == ErrorType.MissingToken)
        }
    }

    @Test
    fun `when token is invalid and button is pressed, set error state`() = runTest(testDispatcher) {
        every { mockGetTokenUseCase.invoke() } returns "INVALID_TOKEN"
        coEvery { mockSendCommandUseCase.invoke(any()) } returns SendCommandResult.NotAuthorized
        initViewModel()

        assert(viewModel.uiState.value == TvRemoteUiState.Loading)
        advanceUntilIdle()
        viewModel.onButtonPressed(command = CommandType.turnOn)
        advanceUntilIdle()
        viewModel.uiState.value.let {
            assert(it is TvRemoteUiState.Error)
            assert((it as TvRemoteUiState.Error).type == ErrorType.InvalidToken)
        }
    }

    private fun initViewModel() {
        viewModel = TvRemoteViewModel(
            newToken = null,
            ioDispatcher = testDispatcher,
            sendCommandUseCase = mockSendCommandUseCase,
            registerTokenUseCase = mockRegisterTokenUseCase,
            getTokenUseCase = mockGetTokenUseCase,
        )
    }
}
