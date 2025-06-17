package com.example.notesmoveo.screen.Home.List

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.notesmoveo.screen.Home.HomeViewModel
import com.example.notesmoveo.screen.component.EmptyListMessage
import com.example.notesmoveo.screen.component.NoteCard
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun NoteInList(
    navigateToNote: (String?) -> Unit,
    needToReFetch: Boolean?
){

    val viewModel = koinViewModel<HomeViewModel>()
    val notes = viewModel.sortedNotesList.collectAsStateWithLifecycle().value
    val isLoading = viewModel.isLoadingNotes

    LaunchedEffect(needToReFetch) {
        if (needToReFetch == true) {
            viewModel.fetchAllUserNote()
        }
    }

    if (notes.isEmpty() && !isLoading){
        EmptyListMessage()

    }else{
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            items(notes) { note ->
                NoteCard(
                    modifier = Modifier
                        .height(126.dp),
                    note = note,
                    onClick = {
                        navigateToNote(note.id)
                    }
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}