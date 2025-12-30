package com.uniumuniu.tv.remote

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.uniumuniu.tv.domain.use_case.ClearTokenUseCase
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PairingActivity : AppCompatActivity() {

    @Inject lateinit var clearTokenUseCase: ClearTokenUseCase

    companion object {
        const val TOKEN_EXTRA = "token"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        val uri = intent.data ?: return

        val token = uri.getQueryParameter(TOKEN_EXTRA)

        if (token != null) {
            startActivity(
                Intent(
                    this@PairingActivity,
                    MainActivity::class.java,
                ).apply {
                    putExtra(TOKEN_EXTRA, token)
                }
            )
        } else {
            clearTokenUseCase()
        }
        finishAffinity()
    }
}
