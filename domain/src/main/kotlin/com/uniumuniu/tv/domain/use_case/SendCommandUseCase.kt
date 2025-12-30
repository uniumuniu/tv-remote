package com.uniumuniu.tv.domain.use_case

import com.uniumuniu.tv.domain.model.CommandType
import com.uniumuniu.tv.domain.model.SendCommandResult
import com.uniumuniu.tv.domain.repository.TvRemoteRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SendCommandUseCase @Inject constructor(
    private val repository: TvRemoteRepository
) {
    suspend operator fun invoke(command: CommandType): SendCommandResult {
        return try {
            repository.sendCommand(command)
        } catch (t: Throwable) {
            println("error: ${t.message}")
            SendCommandResult.UnknownError
        }
    }
}