package com.example.notes_fkn

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.notes_fkn.ui.theme.Notes_fknTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Notes_fknTheme {
                val notes = listOf(
                    Note(1, "Einkauf", "Milch, Brot, Eier"),
                    Note(2, "Idee", "Notiz-App mit Compose bauen")
                )

                NotesList(
                    notes = notes,
                    onAddNote = {
                        // später: Navigation
                    },
                    onNoteClick = {
                        // später: Bearbeiten
                    }
                )
            }
        }
    }
}

data class Note(
    val id: Long,
    val title: String,
    val content: String
)

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

@Composable
fun NoteItem(
    note: Note,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = note.title,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = note.content,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Notes_fknTheme {
        val notes = listOf(
            Note(1, "Einkauf", "Milch, Brot, Eier"),
            Note(2, "Idee", "Notiz-App mit Compose bauen")
        )

        NotesList(
            notes = notes,
            onAddNote = {
                // später: Navigation
            },
            onNoteClick = {
                // später: Bearbeiten
            }
        )
    }
}

@Composable
fun NotesTopBar(textInput: String) {
    Surface(
        tonalElevation = 3.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = textInput,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}
