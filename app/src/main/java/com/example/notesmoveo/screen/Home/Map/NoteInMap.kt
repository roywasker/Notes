package com.example.notesmoveo.screen.Home.Map

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.notesmoveo.screen.Home.HomeViewModel
import com.example.notesmoveo.screen.component.EmptyListMessage
import com.example.notesmoveo.ui.theme.GrayDarker
import com.example.notesmoveo.utils.Resources
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MarkerInfoWindow
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun NoteInMap(
    navigateToNote: (String?) -> Unit,
    needToReFetch: Boolean?,
    drawerState: DrawerState,
    isDrawerOpen: Boolean
){
    val viewModel = koinViewModel<HomeViewModel>()
    val notes = viewModel.sortedNotesList.collectAsStateWithLifecycle().value
    val isLoading = viewModel.isLoadingNotes
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(needToReFetch) {
        if (needToReFetch == true) {
            viewModel.fetchAllUserNote()
        }
    }

    BackHandler(enabled = isDrawerOpen) {
        coroutineScope.launch {
            drawerState.close()
        }
    }

    if (notes.isEmpty() && !isLoading) {

        EmptyListMessage()

    }else if (notes.isNotEmpty()){
        val firstNote = notes.first()
        val location = LatLng(firstNote.latitude,firstNote.longitude )
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(location, 12f)
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Text(
                modifier = Modifier
                    .padding(top = 4.dp),
                text = Resources.Note.mapTitle,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                color = Color.Black.copy(0.8f)
            )

            Box(
                Modifier
                    .fillMaxSize()
                    .padding(
                        vertical = 16.dp,
                        horizontal = 6.dp
                    )
                    .border(
                        BorderStroke(
                            1.dp,
                            GrayDarker
                        )
                    )
            ) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                ) {
                    notes.forEach { note ->

                        val position = LatLng(note.latitude, note.longitude)

                        MarkerInfoWindow(
                            state = MarkerState(position = position),
                            onInfoWindowClick = {
                                navigateToNote(note.id)
                            }
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier
                                    .border(
                                        BorderStroke(1.dp, Color.Black),
                                        RoundedCornerShape(12)
                                    )
                                    .clip(RoundedCornerShape(12))
                                    .background(GrayDarker)
                                    .padding(20.dp)
                            ) {
                                Text(
                                    note.title,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.White
                                )
                                Text(
                                    note.body,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}