package com.example.notesmoveo.domain.impl

import android.net.Uri
import com.example.notesmoveo.data.model.Notes
import com.example.notesmoveo.domain.repository.NoteRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.withTimeout
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import com.google.firebase.storage.storage
import kotlinx.coroutines.tasks.await

class NoteRepositoryImpl: NoteRepository {

    override suspend fun createNote(
        note: Notes,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ){
        try {
            val currentUserUid = Firebase.auth.currentUser?.uid
            if (currentUserUid == null) {
                onError("User not authenticated")
                return
            }

            val firestore = Firebase.firestore
            val noteCollection = firestore.collection("note")

            val noteToCreate = note.copy(
                userId = currentUserUid
            )
            noteCollection.document(note.id).set(noteToCreate).await()
            onSuccess()
        } catch (e: Exception){
            onError("Error while creating new note: ${e.message}")
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun uploadImageToStorage(uri: Uri): String? {
        val currentUserUid = Firebase.auth.currentUser?.uid
        return if (currentUserUid != null) {
            val storage = Firebase.storage.reference
            val imagePath = storage.child("images/${Uuid.random().toHexString()}")
            try {
                withTimeout(20000L) {
                    imagePath.putFile(uri).await()
                    imagePath.downloadUrl.await().toString()
                }
            } catch (e: Exception) {
                null
            }
        } else null
    }

    override suspend fun getUserNotes(userId: String): List<Notes> {
        return try {
            val snapshot = Firebase.firestore
                .collection("note")
                .whereEqualTo("userId", userId)
                .get()
                .await()

            snapshot.documents.mapNotNull { document ->
                document.toObject(Notes::class.java)
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getNoteById(noteId: String): Notes? {
        return try {
            val snapshot = Firebase.firestore
                .collection("note")
                .document(noteId)
                .get()
                .await()
            snapshot.toObject(Notes::class.java)
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun deleteNoteById(
        noteId: String,
        noteImagePath: String?,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        return try {
            Firebase.firestore
                .collection("note")
                .document(noteId)
                .delete()
                .await()
            if (noteImagePath != null) {
                val stringToFind = "images%2F"
                val startIndex = noteImagePath.indexOf(stringToFind)+stringToFind.length
                val subImagePath = noteImagePath.substring(startIndex,startIndex+32)
                val imagePath ="images/$subImagePath"
                Firebase.storage.reference.child(imagePath).delete().await()
            }
            onSuccess()
        }catch (e : Exception){
            onError("Error while deleting note: ${e.message}")
        }
    }

    override suspend fun updateNote(note: Notes, onSuccess: () -> Unit, onError: (String) -> Unit) {
        return try {
            val snapshot = Firebase.firestore
                .collection("note")
                .document(note.id)
                .set(note)
                .await()
            onSuccess()
        }catch (e : Exception){
            onError("Error while updating note: ${e.message}")
        }
    }
}