package com.example.notes_fkn.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.notes_fkn.model.Note
import com.example.notes_fkn.ui.editnote.EditNoteScreen
import com.example.notes_fkn.ui.noteslist.NotesList

@Composable
fun NotesApp() {

    var notes by remember { mutableStateOf(listOf<Note>()) }
    var editingNote by remember { mutableStateOf<Note?>(null) }
    var isEditing by remember { mutableStateOf(false) }

    if (isEditing) {
        EditNoteScreen(
            note = editingNote,
            onSave = { note ->
                notes = notes
                    .filterNot { it.id == note.id }
                    .plus(note)
                isEditing = false
                editingNote = null
            },
            onCancel = {
                isEditing = false
                editingNote = null
            }
        )
    } else {
        NotesList(
            notes = notes,
            onAddNote = {
                editingNote = null
                isEditing = true
            },
            onNoteClick = { note ->
                editingNote = note
                isEditing = true
            }
        )
    }
}
