package com.example.notesmoveo.screen.Home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.notesmoveo.screen.Home.List.NoteInList
import com.example.notesmoveo.screen.Home.Map.NoteInMap
import com.example.notesmoveo.screen.Home.domain.BottomBarDestination
import com.example.notesmoveo.screen.Home.domain.DrawerItem
import com.example.notesmoveo.screen.component.DrawerItemCard
import com.example.notesmoveo.screen.navigation.Screen
import com.example.notesmoveo.ui.theme.Orange
import com.example.notesmoveo.utils.Resources
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToLogin: () -> Unit,
    navigateToNote: (String?) -> Unit,
    rootNavController: NavHostController
){

    val viewModel = koinViewModel<HomeViewModel>()
    val navController = rememberNavController()
    val coroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val isDrawerOpen by remember { derivedStateOf { drawerState.isOpen } }
    var isLoading by remember { mutableStateOf(false) }
    val isLoadingNotes = viewModel.isLoadingNotes
    val needRefresh = remember { rootNavController.currentBackStackEntry?.savedStateHandle?.remove<Boolean>("needToReFetch") }
    val currentRoute = navController.currentBackStackEntryAsState()

    val selectedNestedDestination by remember {
        derivedStateOf {
            val route = currentRoute.value?.destination?.route.toString()
            when{
                route.contains(BottomBarDestination.List.screen.toString()) -> BottomBarDestination.List
                route.contains(BottomBarDestination.Map.screen.toString()) -> BottomBarDestination.Map
                else -> BottomBarDestination.List
            }
        }
    }

    val topBarTitle = when (selectedNestedDestination) {
        BottomBarDestination.List -> "Home"
        BottomBarDestination.Map -> "Map"
    }

    BackHandler(enabled = isDrawerOpen) {
        coroutineScope.launch { drawerState.close() }
    }


    if (isLoading || isLoadingNotes) {
        Popup(
            alignment = Alignment.Center,
        ) {
            CircularProgressIndicator(color = Orange)
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = isDrawerOpen, // Enable only to  close drawer with gestures
        drawerContent = {
            ModalDrawerSheet{
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(0.6f)
                        .padding(horizontal = 12.dp)
                ) {
                    Spacer(modifier = Modifier.height(60.dp))

                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = Resources.Home.sideBarTitle,
                        textAlign = TextAlign.Center,
                        fontSize = 40.sp,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(50.dp))

                    DrawerItem.entries.forEach { item ->
                        DrawerItemCard(
                            drawerItem = item,
                            onClick = {
                                when (item) {
                                    DrawerItem.Note -> {
                                        navigateToNote(null)
                                    }
                                    DrawerItem.SignOut -> {
                                        isLoading = true
                                        viewModel.logout(
                                            onSuccess = {
                                                isLoading = false
                                                navigateToLogin()
                                            },
                                            onError = {
                                                isLoading = false
                                            }
                                        )
                                    }
                                }
                                coroutineScope.launch { drawerState.close() }
                            }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        },
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = topBarTitle,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Medium,
                            color = Orange
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            coroutineScope.launch {
                                drawerState.apply {
                                    if (isClosed) open() else close()
                                }
                            }
                        }) {
                            Icon(
                                modifier = Modifier.size(size = 26.dp),
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu icon",
                            )
                        }
                    }
                )
            },
            floatingActionButton = {
                if (selectedNestedDestination != BottomBarDestination.Map) {
                    FloatingActionButton(
                        modifier = Modifier
                            .size(size = 60.dp),
                        onClick = {
                            navigateToNote(null)
                        },
                        containerColor = Orange,
                        contentColor = Color.Black,
                        content = {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add icon"
                            )
                        }
                    )
                }
            },
            bottomBar = {
                NavigationBar {
                    BottomBarDestination.entries.forEach { destination ->
                        NavigationBarItem(
                            selected = selectedNestedDestination == destination,
                            onClick = {
                                navController.navigate(destination.screen) {
                                    launchSingleTop = true
                                    popUpTo<Screen.ListView> {
                                        saveState = true
                                        inclusive = false
                                    }
                                    restoreState = true
                                }
                            },
                            label = { Text(destination.title) },
                            icon = { Icon(imageVector = destination.icon, contentDescription = "${destination.title} icon") }
                        )
                    }
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                val userData = viewModel.user

                Text(
                    text = "${Resources.Home.welcome} , " +
                            (userData?.firstName?: "") +
                            " ${userData?.lastName?: ""} ",
                    fontSize = 24.sp,
                )
                NavHost(
                    modifier = Modifier.weight(1f),
                    navController = navController,
                    startDestination = Screen.ListView
                ) {
                    composable<Screen.ListView> {
                        NoteInList(
                            navigateToNote = navigateToNote,
                            needToReFetch = needRefresh
                        )
                    }
                    composable<Screen.MapView> {
                        NoteInMap(
                            navigateToNote = navigateToNote,
                            needToReFetch = needRefresh,
                            drawerState = drawerState,
                            isDrawerOpen = isDrawerOpen,
                        )
                    }
                }
            }
        }
    }
}