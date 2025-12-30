package com.uniumuniu.tv.remote.data

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.uniumuniu.tv.domain.repository.TokenRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import androidx.core.content.edit

class TokenRepositoryImpl @Inject constructor(
    @ApplicationContext context: Context
) : TokenRepository {

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val prefs: SharedPreferences =
        EncryptedSharedPreferences.create(
            context,
            FILE_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

    override fun saveToken(token: String) {
        prefs.edit {
            putString(KEY_TOKEN, token)
        }
    }

    override fun getToken(): String? =
        prefs.getString(KEY_TOKEN, null)

    override fun clearToken() {
        prefs.edit {
            remove(KEY_TOKEN)
        }
    }

    companion object {
        private const val FILE_NAME = "secure_auth_prefs"
        private const val KEY_TOKEN = "auth_token"
    }
}
