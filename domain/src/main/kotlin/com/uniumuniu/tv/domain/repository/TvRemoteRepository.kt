package com.uniumuniu.tv.domain.repository

import com.uniumuniu.tv.domain.model.CommandType
import com.uniumuniu.tv.domain.model.SendCommandResult

interface TvRemoteRepository {
    suspend fun sendCommand(command: CommandType) : SendCommandResult
}