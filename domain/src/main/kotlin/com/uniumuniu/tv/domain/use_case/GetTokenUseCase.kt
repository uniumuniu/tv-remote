package com.uniumuniu.tv.domain.use_case

import com.uniumuniu.tv.domain.repository.TokenRepository
import javax.inject.Inject

class GetTokenUseCase @Inject constructor(
    private val tokenRepository: TokenRepository,
) {
    operator fun invoke(): String? {
        return try {
            tokenRepository.getToken()
        } catch (t: Throwable) {
            println("error: ${t.message}")
            null
        }
    }
}
