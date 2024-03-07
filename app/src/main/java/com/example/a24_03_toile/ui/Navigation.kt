package com.example.a24_03_toile.ui

import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.a24_03_toile.MainActivity2
import com.example.a24_03_toile.model.PictureBean
import com.example.a24_03_toile.ui.screens.DetailScreen
import com.example.a24_03_toile.ui.screens.SearchScreen
import com.example.a24_03_toile.viewmodel.MainViewModel

//sealed permet de dire qu'une classe est héritable (ici par SearchScreen et DetailScreen)
//Uniquement par les sous classes qu'elle contient
sealed class Routes(val route: String) {
    //Route 1
    data object SearchScreen : Routes("homeScreen")

    //Route 2 avec paramètre
    data object DetailScreen : Routes("detailScreen/{id}") {
        //Méthode(s) qui vienne(nt) remplit le ou les paramètres
        fun withId(id: Int) = "detailScreen/$id"

        fun withObject(data: PictureBean) = "detailScreen/${data.id}"
    }
}

@Composable
fun AppNavigation() {

    val navController: NavHostController = rememberNavController()

    val mainViewModel: MainViewModel = viewModel()

    Column {

        val context = LocalContext.current

        Button(
            onClick = {context.startActivity(Intent(context, MainActivity2::class.java))},
            contentPadding = ButtonDefaults.ButtonWithIconContentPadding
        ) {
            Icon(
                Icons.Filled.AccountBox,
                contentDescription = "Localized description",
                modifier = Modifier.size(ButtonDefaults.IconSize)
            )
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            Text("Go to XML Activity")
        }

        //Import version avec Composable
        NavHost(navController = navController, startDestination = Routes.SearchScreen.route) {
            //Route 1 vers notre SearchScreen
            composable(Routes.SearchScreen.route) {
                //on peut passer le navController à un écran s'il déclenche des navigations
                SearchScreen(navController, mainViewModel)
            }

            //Route 2 vers un écran de détail
            composable(
                route = Routes.DetailScreen.route,
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) {
                val id = it.arguments?.getInt("id") ?: 1
                DetailScreen(id, navController, mainViewModel)
            }
        }
    }
}