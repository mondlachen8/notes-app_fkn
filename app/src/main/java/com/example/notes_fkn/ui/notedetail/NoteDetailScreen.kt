package com.example.notes_fkn.ui.notedetail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.notes_fkn.model.Note
import com.example.notes_fkn.ui.components.NoteDetailTopBar
import com.example.notes_fkn.ui.editnote.buildAnnotatedContent

@Composable
fun NoteDetailScreen(
    note: Note,
    onBack: () -> Unit,
    onEdit: () -> Unit,
    onDeleteConfirmed: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            NoteDetailTopBar(
                title = note.title,
                onBack = onBack,
                onEdit = onEdit,
                //onDelete = onDeleteConfirmed
                onDelete = {
                    showDeleteDialog = true
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text(
                text = note.title,
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                //text = note.content,
                text = buildAnnotatedContent(note.content, note.spans),
                style = MaterialTheme.typography.bodyLarge
            )
            // ----- LÖSCHDIALOG -----
            if (showDeleteDialog) {
                AlertDialog(
                    onDismissRequest = { showDeleteDialog = false },
                    title = { Text("Notiz löschen") },
                    text = { Text("Möchtest du diese Notiz wirklich löschen?") },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                showDeleteDialog = false
                                onDeleteConfirmed()
                            }
                        ) {
                            Text("Löschen")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = { showDeleteDialog = false }
                        ) {
                            Text("Abbrechen")
                        }
                    }
                )
            }
        }
    }
}
