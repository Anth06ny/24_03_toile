package com.example.a24_03_toile.ui.screens

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.example.a24_03_toile.R
import com.example.a24_03_toile.ui.MyTopBar
import com.example.a24_03_toile.ui.theme._24_03_toileTheme
import com.example.a24_03_toile.viewmodel.MainViewModel

@Preview(showBackground = true, showSystemUi = true)
@Preview(showBackground = true, showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun DetailScreenPreview() {
    _24_03_toileTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {


            DetailScreen(1)
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable //id du PictureBean à afficher
fun DetailScreen(idPicture: Int, navController: NavHostController? = null, mainViewModel: MainViewModel = viewModel()) {

    val pictureBean = mainViewModel.myList.find { it.id == idPicture }

    Column(
        modifier = Modifier
            .padding(8.dp)
    ) {
        MyTopBar(
            title = pictureBean?.title ?: "-",
            navController = navController,
            //Icônes sur la barre
            topBarActions = listOf {
                IconButton(onClick = {
                    mainViewModel.togglePictureAt(idPicture)
                }) {
                    Icon(
                        if (pictureBean?.favorite == true) Icons.Filled.Favorite else Icons.Default.FavoriteBorder, contentDescription = "Coeur"
                    )
                }
            }
        )

        Text(
            text = pictureBean?.title ?: "Pas de donnée",
            fontSize = 36.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.fillMaxWidth()
        )

        if (pictureBean != null) {
            GlideImage(
                model = pictureBean.url,
                contentDescription = "une photo de chat",
                loading = placeholder(R.mipmap.ic_launcher_round),
                failure = placeholder(R.mipmap.ic_launcher),
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        }

        Text(
            text = pictureBean?.longText ?: "Pas de donnée",
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
        Spacer(Modifier.size(16.dp))

        Button(
            onClick = { navController?.popBackStack() },
            contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
            modifier = Modifier.align(alignment = Alignment.CenterHorizontally)


        ) {
            Icon(
                Icons.Filled.ArrowBack,
                contentDescription = "Localized description",
                modifier = Modifier.size(ButtonDefaults.IconSize)
            )
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            Text("Retour")
        }
    }


}