package com.example.a24_03_toile.ui

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.a24_03_toile.ui.theme._24_03_toileTheme

@Preview(showBackground = true, showSystemUi = true)
@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun MyErrorPreview() {
    //Il faut remplacer NomVotreAppliTheme par le thème de votre application
    //Utilisé par exemple dans MainActivity.kt sous setContent {...}
    _24_03_toileTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Column {
                MyError(errorMessage = "Avec message d'erreur")
                Text("Sans erreur : ")
                MyError(errorMessage = "")
                Text("----------")
            }

        }
    }
}

@Composable
fun MyError(
    modifier: Modifier = Modifier,
    errorMessage: String? = null,
    onRetryClick: () -> Unit = {}
) {
    AnimatedVisibility(!errorMessage.isNullOrBlank()) {
        Row(
            modifier = modifier
                .background(MaterialTheme.colorScheme.error)
                .padding(8.dp)

        ) {

            Text(
                text = errorMessage ?: "",
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onError,
                modifier = Modifier.weight(1f)
            )

            Text(
                text = "Retry",
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onError,
                modifier = Modifier.clickable(onClick = onRetryClick)
            )
        }
    }

}