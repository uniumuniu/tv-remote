package com.uniumuniu.tv.domain.use_case

import com.uniumuniu.tv.domain.repository.TokenRepository
import javax.inject.Inject

class ClearTokenUseCase @Inject constructor(
    private val tokenRepository: TokenRepository,
) {
    operator fun invoke() {
        try {
            tokenRepository.clearToken()
        } catch (t: Throwable) {
            println("error: ${t.message}")
        }
    }
}