package com.example.notes_fkn.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.example.notes_fkn.data.NotesFileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.example.notes_fkn.model.Note

class NotesViewModel(
    application: Application
) : AndroidViewModel(application) {
    /**
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
    */
    private val repository = NotesFileRepository(application)

    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes

    init {
        _notes.value = repository.loadNotes()
    }
    fun saveNote(note: Note) {
        _notes.value = _notes.value
            .filterNot { it.id == note.id } + note

        repository.saveNotes(_notes.value)
    }
    fun deleteNote(noteId: Long) {
        _notes.value = _notes.value.filterNot { it.id == noteId }
        repository.saveNotes(_notes.value)
    }
    fun getNoteById(id: Long): Note? {
        return _notes.value.find { it.id == id }
    }
}

