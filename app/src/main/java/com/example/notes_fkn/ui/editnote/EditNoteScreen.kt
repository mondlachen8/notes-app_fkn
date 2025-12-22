package com.example.notes_fkn.ui.editnote

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatBold
import androidx.compose.material.icons.filled.FormatItalic
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.notes_fkn.model.Note

@Composable
fun EditNoteScreen(
    note: Note?,
    onSave: (Note) -> Unit,
    onCancel: () -> Unit
) {
    var title by rememberSaveable { mutableStateOf(note?.title ?: "") }
    var content by rememberSaveable { mutableStateOf(note?.content ?: "") }
    var textSize by rememberSaveable { mutableStateOf(16.sp) }

    var textFieldValue by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(note?.content ?: ""))
    }
    fun applyStyle(style: Style) {
        val start = textFieldValue.selection.start
        val end = textFieldValue.selection.end

        if (start == end) return // keine Auswahl

        val builder = AnnotatedString.Builder(textFieldValue.text)
        when (style) {
            Style.BOLD -> builder.addStyle(SpanStyle(fontWeight = FontWeight.Bold), start, end)
            Style.ITALIC -> builder.addStyle(SpanStyle(fontStyle = FontStyle.Italic), start, end)
        }

        textFieldValue = textFieldValue.copy(text = builder.toAnnotatedString().text)
    }


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
            Text("Textgröße: ${textSize.value.toInt()}sp")
            Slider(
                value = textSize.value,
                onValueChange = { textSize = it.sp },
                valueRange = 12f..36f
            )


            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("Inhalt") },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                textStyle = TextStyle(fontSize = textSize),
                maxLines = Int.MAX_VALUE
            )

            Row {
                IconButton(onClick = { applyStyle(Style.BOLD) }) {
                    Icon(Icons.Default.FormatBold, contentDescription = "Fett")
                }
                IconButton(onClick = { applyStyle(Style.ITALIC) }) {
                    Icon(Icons.Default.FormatItalic, contentDescription = "Kursiv")
                }
            }
        }
    }
}

enum class Style { BOLD, ITALIC }
