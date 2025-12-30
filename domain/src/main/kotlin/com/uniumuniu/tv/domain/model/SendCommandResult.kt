package com.uniumuniu.tv.domain.model

sealed class SendCommandResult {
    object Success : SendCommandResult()
    object NotAuthorized : SendCommandResult()
    object MissingToken : SendCommandResult()
    object UnknownError : SendCommandResult()
}
