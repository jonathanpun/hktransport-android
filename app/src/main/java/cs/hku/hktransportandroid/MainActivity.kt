package cs.hku.hktransportandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import cs.hku.hktransportandroid.screen.Home
import cs.hku.hktransportandroid.screen.Search
import cs.hku.hktransportandroid.screen.Stop

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val items = listOf(
                Screen.Home to Icons.Filled.Home,
                Screen.Search to Icons.Filled.Search,
            )
            val navController = rememberNavController()
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            Scaffold(
                bottomBar = {when(navBackStackEntry?.destination?.route){
                    in (items.map { it.first.route })-> BottomNavigation {
                        val currentDestination = navBackStackEntry?.destination
                        items.forEach { screen ->
                            BottomNavigationItem(
                                icon = { Icon(screen.second, contentDescription = null) },
                                label = { Text(stringResource(screen.first.resourceId)) },
                                selected = currentDestination?.hierarchy?.any { it.route == screen.first.route } == true,
                                onClick = {
                                    navController.navigate(screen.first.route) {
                                        // Pop up to the start destination of the graph to
                                        // avoid building up a large stack of destinations
                                        // on the back stack as users select items
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        // Avoid multiple copies of the same destination when
                                        // reselecting the same item
                                        launchSingleTop = true
                                        // Restore state when reselecting a previously selected item
                                        restoreState = true
                                    }
                                }
                            )
                        }
                    }
                }
                }
            ) { innerPadding ->
                NavHost(navController, startDestination = Screen.Home.route, Modifier.padding(innerPadding)) {
                    composable(Screen.Home.route) { Home(navController) }
                    composable(Screen.Search.route) { Search(navController) }
                    composable(Screen.Stop.route){navBackStackEntry ->  Stop(navController,navBackStackEntry.arguments?.getString("Id").orEmpty())}
                }
            }
        }
    }

    sealed class Screen(val route: String, @StringRes val resourceId: Int) {
        object Home : Screen("home", R.string.home)
        object Search : Screen("search", R.string.search)
        object Stop:Screen("stop/{Id}",R.string.stop)
    }




}