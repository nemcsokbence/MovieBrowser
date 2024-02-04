package com.mbh.moviebrowser.ui

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import com.mbh.moviebrowser.R
import com.mbh.moviebrowser.util.ErrorHandler.getErrorMessageResource
import com.mbh.moviebrowser.util.ErrorHandler.getErrorTitleRes
import com.mbh.moviebrowser.util.Resource
import kotlinx.coroutines.flow.SharedFlow

@Composable
fun MovieAlertDialog(error: SharedFlow<Resource.Error?>, onDismiss: () -> Unit) {

    val errorState by error.collectAsState(null)

    if (errorState != null) {
        AlertDialog(
            onDismissRequest = {
                onDismiss()
            },
            title = {
                Text(text = stringResource(errorState?.getErrorTitleRes() ?: R.string.unknown_error_title))
            },
            text = {
                Text(stringResource(errorState?.getErrorMessageResource() ?: R.string.unknown_error_message))
            },
            confirmButton = {
                Button(

                    onClick = {
                        onDismiss()
                    }) {
                    Text(stringResource(R.string.ok))
                }
            }
        )
    }
}