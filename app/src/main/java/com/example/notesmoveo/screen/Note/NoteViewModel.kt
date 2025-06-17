package com.example.notesmoveo.screen.Note

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesmoveo.data.model.Notes
import com.example.notesmoveo.data.model.RequestState
import com.example.notesmoveo.domain.repository.NoteRepository
import com.example.notesmoveo.utils.LocationProvider
import kotlinx.coroutines.launch

class NoteViewModel(
    private val noteRepository: NoteRepository,
    private val locationProvider: LocationProvider
): ViewModel() {

    var noteState: Notes by mutableStateOf(Notes())
        private set

    var uploadImageState: RequestState<Unit> by mutableStateOf(RequestState.Idle)
        private set

    val isFormValid: Boolean
        get() = noteState.title.isNotEmpty() &&
                noteState.body.isNotEmpty()

    fun updateTitle(value: String) {
        noteState = noteState.copy(title = value)
    }

    fun updateBody(value: String) {
        noteState = noteState.copy(body = value)
    }

    fun updateImageUploaderState(value: RequestState<Unit>){
        uploadImageState = value
    }

    fun updateImage(image: String?){
        noteState = noteState.copy(imageUrl = image)
    }

    fun uploadImageToStorage(
        uri: Uri?,
        onSuccess: () -> Unit
    ){

        if (uri == null){
            updateImageUploaderState(RequestState.Error("File not fond"))
            return
        }

        updateImageUploaderState(RequestState.Loading)

        viewModelScope.launch {
            try {
                val downloadUrl = noteRepository.uploadImageToStorage(uri)
                if (downloadUrl.isNullOrEmpty()){
                    throw Exception("Failed to retrieve a download URL after the uploaded")
                }

                updateImage(downloadUrl)
                updateImageUploaderState(RequestState.Success(Unit))
                onSuccess()

            } catch (e: Exception){
                updateImageUploaderState(RequestState.Error("Error while uploading image: ${e.message}"))
            }
        }
    }

    fun createNote(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ){
        viewModelScope.launch {
            val location = locationProvider.getCurrentLocation()
            noteRepository.createNote(
                Notes(
                    title = noteState.title,
                    body = noteState.body,
                    imageUrl = noteState.imageUrl,
                    latitude = location?.latitude ?: 32.0624668,
                    longitude = location?.longitude ?: 34.7719214
                ),
                onSuccess = onSuccess,
                onError = onError
            )
        }
    }

    fun fetchNoteById(
        id: String,
    ){
        viewModelScope.launch {
            val note = noteRepository.getNoteById(id)
            if (note != null) {
                noteState = note
                if (note.imageUrl != null) {
                    updateImageUploaderState(RequestState.Success(Unit))
                }
            }
        }
    }

    fun deleteNoteById(
        id: String,
        noteImagePath: String?,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ){
        viewModelScope.launch {
            noteRepository.deleteNoteById(
                noteId = id,
                noteImagePath = noteImagePath,
                onSuccess = onSuccess,
                onError = onError
            )
        }
    }

    fun updateNote(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ){
        viewModelScope.launch {
            noteRepository.updateNote(
                Notes(
                    id = noteState.id,
                    userId = noteState.userId,
                    date = noteState.date,
                    title = noteState.title,
                    body = noteState.body,
                    imageUrl = noteState.imageUrl,
                    latitude = noteState.latitude,
                    longitude = noteState.longitude
                ),
                onSuccess = onSuccess,
                onError = onError
            )
        }
    }
}