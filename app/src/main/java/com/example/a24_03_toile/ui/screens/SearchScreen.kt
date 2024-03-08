package com.example.a24_03_toile.ui.screens

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.example.a24_03_toile.R
import com.example.a24_03_toile.model.PictureBean
import com.example.a24_03_toile.ui.MyError
import com.example.a24_03_toile.ui.MyTopBar
import com.example.a24_03_toile.ui.Routes
import com.example.a24_03_toile.ui.theme._24_03_toileTheme
import com.example.a24_03_toile.viewmodel.FakeViewModel
import com.example.a24_03_toile.viewmodel.MainViewModel

@Preview(showBackground = true, showSystemUi = true)
@Preview(showBackground = true, showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun SearchScreenPreview() {
    //Il faut remplacer NomVotreAppliTheme par le thème de votre application
    //Utilisé par exemple dans MainActivity.kt sous setContent {...}
    _24_03_toileTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {

            SearchScreen(
                mainViewModel = FakeViewModel()
            )
        }
    }
}

@Composable
fun SearchScreen(navHostController : NavHostController? = null, mainViewModel : MainViewModel = viewModel()) {

    var showFavorite by remember { mutableStateOf(false) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        MyTopBar(
            title = "Météo",
            navController = navHostController,
            //Icônes sur la barre
            topBarActions = listOf {
                IconButton(onClick = {
                    showFavorite = !showFavorite
                }) {
                    Icon(
                        if (showFavorite) Icons.Filled.Favorite else Icons.Default.FavoriteBorder, contentDescription = "Coeur"
                    )
                }
                IconButton(onClick = {
                }) {
                    Icon(
                        Icons.Filled.LocationOn, contentDescription = "Coeur"
                    )
                }
            },
            dropDownMenuItem  = listOf(
                Triple(Icons.Filled.Clear, "Clear") { mainViewModel.uploadSearchText("") }
            )

        )

        SearchBar(value = mainViewModel.searchText) {
            mainViewModel.uploadSearchText(it)
        }
        AnimatedVisibility(visible = mainViewModel.runInProgress) {
            CircularProgressIndicator()
        }

        MyError(
            errorMessage =  mainViewModel.errorMessage,
            onRetryClick = mainViewModel.lastAction
            )

        //Permet de remplacer très facilement le RecyclerView. LazyRow existe aussi
        LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            //val filterList = mainViewModel.myList.filter { it.title.contains(mainViewModel.searchText) }
            val filterList = mainViewModel.myList.filter { !showFavorite || it.favorite }

            items(filterList.size) {
                PictureRowItem(data =filterList[it] , onPictureClick = {
                    navHostController?.navigate(Routes.DetailScreen.withObject(filterList[it]))
                })
            }
        }
        Row {
            Button(
                onClick = { mainViewModel.uploadSearchText("") },
                contentPadding = ButtonDefaults.ButtonWithIconContentPadding
            ) {
                Icon(
                    Icons.Filled.Clear,
                    contentDescription = "Localized description",
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text("Clear filter")
            }


            Button(
                onClick = { mainViewModel.loadData() },
                contentPadding = ButtonDefaults.ButtonWithIconContentPadding
            ) {
                Icon(
                    Icons.Filled.Send,
                    contentDescription = "Localized description",
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text("Load data")
            }
        }
    }
}

//fun SearchBar(modifier: Modifier = Modifier, searchText : MutableState<String>) {
@Composable
fun SearchBar(modifier: Modifier = Modifier, value: String = "", onValueChanged : (String) -> Unit = {}) {

    TextField(
        value = value, //Valeur par défaut
        onValueChange = onValueChanged, //Action
        leadingIcon = { //Image d'icone
            Icon(
                imageVector = Icons.Default.Search,
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = null
            )
        },
        label = { Text("Rechercher") }, //Texte d'aide qui se déplace
        //Comment le composant doit se placer
        modifier = modifier
            .fillMaxWidth() // Prend toute la largeur
            .heightIn(min = 56.dp) //Hauteur minimum
    )
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PictureRowItem(modifier: Modifier = Modifier, data: PictureBean, onPictureClick: () -> Unit = {}) {

    //Persiste à la recomposition
    var isExpanded by remember { mutableStateOf(false) }

    Row(
        modifier
            .background(Color.White)
    ) {
        GlideImage(
            model = data.url,
            contentDescription = null,
            loading = placeholder(R.mipmap.ic_launcher_round),
            failure = placeholder(R.mipmap.ic_launcher),
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .width(100.dp)
                .height(100.dp)
                .clickable(onClick = onPictureClick)
        )

        Column(
            Modifier
                .weight(1f)
                .background(Color.Yellow)
                .clickable { isExpanded = !isExpanded }) {
            Text(
                text = data.title,
                fontSize = 20.sp,
                color = Color.Black,
                modifier = Modifier
                    //.padding(8.dp)
                    .fillMaxWidth()
            )
            Text(
                text = if (isExpanded) data.longText else (data.longText.take(20) + "..."),
                fontSize = 14.sp,
                color = Color.Blue,
                modifier = Modifier
                    //.padding(8.dp)
                    .heightIn(min = 70.dp)
                    .fillMaxSize()
                    .background(Color.Magenta)
                    .animateContentSize()  //Pour l'animation d'agrandissement
            )
        }
    }
}