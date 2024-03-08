package com.example.a24_03_toile.ui

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
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

@Preview(showBackground = true, showSystemUi = true)
@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun MyTopBarPreview() {
    //Il faut remplacer NomVotreAppliTheme par le thème de votre application
    //Utilisé par exemple dans MainActivity.kt sous setContent {...}
    _24_03_toileTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Column {
                MyTopBar("Test" )
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopBar(
    title:String? = null,
    navController: NavHostController? = null,
    //Icône
    topBarActions: List<@Composable () -> Unit>? = null,
    //Icône, Texte, Action
    dropDownMenuItem: List<Triple<ImageVector, String, () -> Unit>>? = null
) {

    var openDropDownMenu by remember { mutableStateOf(false) }

    TopAppBar(
        title = { Text(text = title ?: "") },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
        ),

        //Icône retour
        navigationIcon = {
            if (navController?.previousBackStackEntry != null) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        },
        //Icône sur la barre
        actions = {
            topBarActions?.forEach { it() }

            //DropDownMenu
            if (dropDownMenuItem != null) {

                //Icône du menu
                IconButton(onClick = { openDropDownMenu = true }) {
                    Icon(Icons.Filled.Menu, contentDescription = null)
                }

                //menu caché qui doit s'ouvrir quand on clique sur l'icône
                DropdownMenu(
                    expanded = openDropDownMenu,
                    onDismissRequest = { openDropDownMenu = false }
                ) {
                    //les items sont constitués à partir de la liste
                    dropDownMenuItem.forEach {
                        DropdownMenuItem(text = {
                            Row {
                                Icon(it.first, contentDescription = "")
                                Text(it.second)
                            }
                        }, onClick = {
                            openDropDownMenu = false //ferme le menu
                            it.third() //action au clic
                        })
                    }
                }
            }
        }
    )
}