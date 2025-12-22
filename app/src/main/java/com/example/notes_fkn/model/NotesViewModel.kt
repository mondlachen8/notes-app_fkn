package com.example.notes_fkn.model

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.example.notes_fkn.model.Note

class NotesViewModel : ViewModel() {

    // MutableStateFlow intern
    private val _notes = MutableStateFlow<List<Note>>(emptyList())

    // Öffentlich: nur lesbar
    val notes: StateFlow<List<Note>> = _notes

    // Neue Notiz erstellen oder bestehende aktualisieren
    fun saveNote(note: Note) {
        _notes.value = _notes.value
            .filterNot { it.id == note.id } + note
    }

    // Notiz nach ID abrufen
    fun getNoteById(id: Long): Note? {
        return _notes.value.find { it.id == id }
    }
}
