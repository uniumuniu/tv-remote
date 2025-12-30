package com.uniumuniu.tv.remote

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.uniumuniu.tv.remote.presentation.TvRemoteScreen
import com.uniumuniu.tv.remote.presentation.TvRemoteViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val token = intent.getStringExtra(PairingActivity.TOKEN_EXTRA)

        setContent {
            TvRemoteTheme {
                Scaffold { paddingValues ->
                    val viewModel: TvRemoteViewModel = hiltViewModel(
                        creationCallback = { factory: TvRemoteViewModel.TvRemoteViewModelFactory ->
                            factory.create(
                                token = token,
                            )
                        }
                    )
                    TvRemoteScreen(
                        viewModel = viewModel,
                        paddingValues = paddingValues,
                        close = {
                            finishAffinity()
                        }
                    )
                }
            }
        }
    }
}
