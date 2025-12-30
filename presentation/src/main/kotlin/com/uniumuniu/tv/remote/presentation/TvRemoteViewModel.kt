package com.uniumuniu.tv.remote.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uniumuniu.tv.domain.model.CommandType
import com.uniumuniu.tv.domain.use_case.RegisterTokenUseCase
import com.uniumuniu.tv.domain.model.SendCommandResult
import com.uniumuniu.tv.domain.use_case.GetTokenUseCase
import com.uniumuniu.tv.domain.use_case.SendCommandUseCase
import com.uniumuniu.tv.remote.presentation.TvRemoteUiState.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = TvRemoteViewModel.TvRemoteViewModelFactory::class)
class TvRemoteViewModel @AssistedInject constructor(
    @Assisted private val newToken: String?,
    private val ioDispatcher: CoroutineDispatcher,
    private val sendCommandUseCase: SendCommandUseCase,
    private val registerTokenUseCase: RegisterTokenUseCase,
    private val getTokenUseCase: GetTokenUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<TvRemoteUiState>(Loading)
    val uiState: StateFlow<TvRemoteUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch(ioDispatcher) {
            // Simulate loading
            delay(2000)

            if (newToken != null) {
                registerTokenUseCase.invoke(newToken)
            }

            if (getTokenUseCase() == null) {
                _uiState.update { Error(ErrorType.MissingToken) }
            } else {
                _uiState.update { Ready }
            }
        }
    }

    fun onButtonPressed(command: CommandType) {
        viewModelScope.launch(ioDispatcher) {
            when (sendCommandUseCase(command)) {
                SendCommandResult.Success ->
                    _uiState.value = Ready

                SendCommandResult.NotAuthorized ->
                    _uiState.value = Error(ErrorType.InvalidToken)

                SendCommandResult.UnknownError ->
                    _uiState.value = Error(ErrorType.GenericError)

                SendCommandResult.MissingToken ->
                    _uiState.value = Error(ErrorType.MissingToken)
            }
        }
    }

    @AssistedFactory
    interface TvRemoteViewModelFactory {
        fun create(token: String?): TvRemoteViewModel
    }
}
