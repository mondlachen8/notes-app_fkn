package com.example.notes_fkn.ui.editnote

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun EditNoteTopBar(
    isEditing: Boolean,
    onSaveClick: () -> Unit,
    onCancelClick: () -> Unit
) {
    Surface(tonalElevation = 3.dp) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(128.dp)
                .padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(onClick = onCancelClick) {
                Text("Abbrechen")
            }

            Text(
                text = if (isEditing) "Notiz bearbeiten" else "Neue Notiz",
                style = MaterialTheme.typography.titleMedium
            )

            TextButton(onClick = onSaveClick) {
                Text("Speichern")
            }
        }
    }
}
