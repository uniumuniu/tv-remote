package com.uniumuniu.tv.domain.repository

interface TokenRepository {
    fun saveToken(token: String)
    fun getToken(): String?
    fun clearToken()
}