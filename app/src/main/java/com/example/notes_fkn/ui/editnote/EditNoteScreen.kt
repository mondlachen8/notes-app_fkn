package com.example.notes_fkn.ui.editnote

import androidx.compose.foundation.layout.*
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.notes_fkn.model.Note

@Composable
fun EditNoteScreen(
    note: Note?,
    onSave: (Note) -> Unit,
    onCancel: () -> Unit
) {
    var title by rememberSaveable { mutableStateOf(note?.title ?: "") }
    var content by rememberSaveable { mutableStateOf(note?.content ?: "") }

    Scaffold(
        topBar = {
            EditNoteTopBar(
                isEditing = note != null,
                onSaveClick = {
                    val editedNote = note?.copy(
                        title = title,
                        content = content
                    ) ?: Note(
                        id = System.currentTimeMillis(),
                        title = title,
                        content = content
                    )
                    onSave(editedNote)
                },
                onCancelClick = onCancel
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Titel") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("Inhalt") },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                maxLines = Int.MAX_VALUE
            )
        }
    }
}
