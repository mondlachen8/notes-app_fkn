package com.example.notes_fkn.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.notes_fkn.data.NotesFileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

private val notesComparator = compareByDescending<Note> { it.isPinned }
    .thenByDescending { it.lastModified }

class NotesViewModel(
    application: Application
) : AndroidViewModel(application) {
    private val repository = NotesFileRepository(application)

    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes

    init {
        val loadedNotes = repository.loadNotes().toMutableList()

        if (loadedNotes.none { it.id == SCHONE_DINGE_NOTE_ID }) {
            loadedNotes.add(
                Note(
                    id = SCHONE_DINGE_NOTE_ID,
                    title = "Schöne Dinge",
                    content = "",
                    spans = emptyList(),
                    lastModified = Long.MAX_VALUE, // damit sie immer oben bleibt
                    isPinned = true
                )
            )
        }
        _notes.value = loadedNotes.sortedWith(notesComparator)
    }
    fun saveNote(note: Note) {
        val existing = _notes.value.find { it.id == note.id }

        val noteToSave = if (existing?.isPinned == true) {
            existing.copy(
                content = note.content,
                lastModified = System.currentTimeMillis()
            )
        } else {
            note.copy(lastModified = System.currentTimeMillis())
        }

        _notes.value = (_notes.value
            .filterNot { it.id == note.id } + noteToSave)
            .sortedWith(notesComparator)

        repository.saveNotes(_notes.value)
    }
    fun deleteNote(noteId: Long) {
        val note = _notes.value.find { it.id == noteId } ?: return
        if (note.isPinned) return

        _notes.value = _notes.value.filterNot { it.id == noteId }
        repository.saveNotes(_notes.value)
    }
    fun getNoteById(id: Long): Note? {
        return _notes.value.find { it.id == id }
    }

    fun updateNoteContent(noteId: Long, newContent: String) {
        val updatedNotes = _notes.value.map { note ->
            if (note.id == noteId) {
                note.copy(
                    content = newContent,
                    lastModified = System.currentTimeMillis()
                )
            } else note
        }.sortedWith(notesComparator)

        _notes.value = updatedNotes
        repository.saveNotes(updatedNotes)
    }

}

