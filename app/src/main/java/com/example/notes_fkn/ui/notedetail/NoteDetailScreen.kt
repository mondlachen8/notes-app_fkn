package com.example.notes_fkn.ui.notedetail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.notes_fkn.model.Note
import com.example.notes_fkn.ui.components.NoteDetailTopBar
import com.example.notes_fkn.ui.editnote.buildAnnotatedContent

@Composable
fun NoteDetailScreen(
    note: Note,
    onBack: () -> Unit,
    onEdit: () -> Unit
) {
    Scaffold(
        topBar = {
            NoteDetailTopBar(
                title = note.title,
                onBack = onBack,
                onEdit = onEdit
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
        }
    }
}
