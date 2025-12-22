package com.example.notes_fkn.ui.noteslist

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import com.example.notes_fkn.model.Note
import com.example.notes_fkn.ui.components.NotesTopBar

@Composable
fun NotesList(
    notes: List<Note>,
    onAddNote: () -> Unit,
    onNoteClick: (Note) -> Unit
) {
    Scaffold(
        topBar = { NotesTopBar("Notizen") },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddNote) {
                Icon(Icons.Default.Add, contentDescription = "Notiz hinzufügen")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues
        ) {
            items(notes) { note ->
                NoteItem(note = note, onClick = { onNoteClick(note) })
            }
        }
    }
}