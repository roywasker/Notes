package com.example.notesmoveo.screen.Home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesmoveo.data.model.Notes
import com.example.notesmoveo.data.model.User
import com.example.notesmoveo.domain.repository.AuthRepository
import com.example.notesmoveo.domain.repository.NoteRepository
import com.example.notesmoveo.domain.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class HomeViewModel (
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val noteRepository: NoteRepository
): ViewModel() {

    private val currentUserUid = authRepository.getCurrentUserId()?: ""

    var user by mutableStateOf<User?>(null)
        private set

    private val _sortedNotesList = MutableStateFlow<List<Notes>>(emptyList())
    val sortedNotesList = _sortedNotesList

    var isLoadingNotes by mutableStateOf(false)

    init {
        fetchUserData()
        fetchAllUserNote()
    }

    private fun fetchUserData() {
        viewModelScope.launch {
            userRepository.getUserById(
                userUid = currentUserUid,
                onSuccess = {
                    user = it
                },
                onError = {}
            )
        }
    }

    fun logout(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            authRepository.signOut(
                onSuccess = onSuccess,
                onError = onError
            )
        }
    }

    fun fetchAllUserNote(){
        isLoadingNotes = true
        viewModelScope.launch {
            val notes = noteRepository.getUserNotes(currentUserUid)
            _sortedNotesList.value = notes.sortedByDescending { it.date }
        }.invokeOnCompletion {
            isLoadingNotes = false
        }
    }
}