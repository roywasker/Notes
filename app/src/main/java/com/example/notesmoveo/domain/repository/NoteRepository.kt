package com.example.notesmoveo.domain.repository

import android.net.Uri
import com.example.notesmoveo.data.model.Notes

interface NoteRepository {

    suspend fun createNote(
        note: Notes,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    )

    suspend fun uploadImageToStorage(uri: Uri): String?

     suspend fun getUserNotes(userId: String): List<Notes>

     suspend fun getNoteById(noteId: String): Notes?

     suspend fun deleteNoteById(
         noteId: String,
         noteImagePath: String?,
         onSuccess: () -> Unit,
         onError: (String) -> Unit
     )

     suspend fun updateNote(
         note: Notes,
         onSuccess: () -> Unit,
         onError: (String) -> Unit
     )
}

