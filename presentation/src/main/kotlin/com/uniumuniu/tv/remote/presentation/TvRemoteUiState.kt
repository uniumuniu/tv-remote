package com.uniumuniu.tv.remote.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

sealed class TvRemoteUiState {
    object Loading : TvRemoteUiState()
    object Ready : TvRemoteUiState()
    class Error(val type: ErrorType) : TvRemoteUiState() {
        val title: String
            @Composable get() = when (type) {
                ErrorType.GenericError -> stringResource(R.string.generic_error_title)
                ErrorType.InvalidToken -> stringResource(R.string.invalid_token_title)
                ErrorType.MissingToken -> stringResource(R.string.missing_token_title)
            }

        val message: String
            @Composable get() = stringResource(R.string.error_message)
    }
}

enum class ErrorType {
    GenericError,
    InvalidToken,
    MissingToken,
}
