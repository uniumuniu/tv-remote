package com.uniumuniu.tv.remote.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uniumuniu.tv.domain.model.CommandType

@Composable
fun TvRemoteScreen(
    viewModel: TvRemoteViewModel,
    paddingValues: PaddingValues,
    close: () -> Unit,
) {
    val state by viewModel.uiState.collectAsState()

    val baseModifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
        .padding(16.dp)

    when (state) {
        TvRemoteUiState.Loading -> Column(
            modifier = baseModifier,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            CircularProgressIndicator()
        }

        TvRemoteUiState.Ready -> SuccessContent(
            modifier = baseModifier,
            onButtonPressed = viewModel::onButtonPressed,
        )

        is TvRemoteUiState.Error -> {
            val errorState = state as TvRemoteUiState.Error
            ErrorContent(
                modifier = baseModifier,
                title = errorState.title,
                message = errorState.message,
                close = close,
            )
        }
    }
}

@Composable
private fun SuccessContent(
    modifier: Modifier = Modifier,
    onButtonPressed: (CommandType) -> Unit,
) {
    ActionsList(
        modifier = modifier,
        actions = CommandType.entries,
        onButtonPressed = onButtonPressed,
    )
}

@Composable
private fun ActionsList(
    actions: List<CommandType>,
    modifier: Modifier = Modifier,
    onButtonPressed: (CommandType) -> Unit,
) {
    LazyColumn(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        items(actions) { action ->
            Button(
                modifier = Modifier.fillMaxSize(),
                onClick = { onButtonPressed(action) }
            ) {
                Text(
                    text = getActionTranslation(action),
                )
            }
        }
    }
}

@Composable
private fun ErrorContent(
    title: String,
    message: String,
    modifier: Modifier = Modifier,
    close: () -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = title,
            style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
            ),
            textAlign = TextAlign.Center,
        )
        Spacer(
            modifier = Modifier.height(8.dp)
        )
        Text(
            text = message,
            style = TextStyle(
                fontSize = 14.sp,
            ),
            textAlign = TextAlign.Center,
        )
        Spacer(
            modifier = Modifier.height(4.dp)
        )
        Button(
            onClick = close,
        ) {
            Text(
                text = stringResource(R.string.close_label),
            )
        }
    }
}

@Composable
@ReadOnlyComposable
private fun getActionTranslation(commandType: CommandType): String {
    return when (commandType) {
        CommandType.turnOn -> stringResource(R.string.turn_on_label)
        CommandType.turnOff -> stringResource(R.string.turn_off_label)
        CommandType.volumeUp -> stringResource(R.string.volume_up_label)
        CommandType.volumeDown -> stringResource(R.string.volume_down_label)
        CommandType.nextChannel -> stringResource(R.string.next_channel_label)
        CommandType.previousChannel -> stringResource(R.string.previous_channel_label)
        CommandType.arrowUp -> stringResource(R.string.arrow_up_label)
        CommandType.arrowDown -> stringResource(R.string.arrow_down_label)
        CommandType.arrowLeft -> stringResource(R.string.arrow_left_label)
        CommandType.arrowRight -> stringResource(R.string.arrow_right_label)
        CommandType.ok -> stringResource(R.string.ok_label)
        CommandType.back -> stringResource(R.string.back_label)
    }
}


