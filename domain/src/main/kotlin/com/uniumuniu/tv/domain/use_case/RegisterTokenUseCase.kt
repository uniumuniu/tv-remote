package com.uniumuniu.tv.domain.use_case

import com.uniumuniu.tv.domain.repository.TokenRepository
import javax.inject.Inject

class RegisterTokenUseCase @Inject constructor(
    private val tokenRepository: TokenRepository,
) {
    operator fun invoke(token: String) {
        try {
            tokenRepository.saveToken(token)
        } catch (t: Throwable) {
            println("error: ${t.message}")
        }
    }
}