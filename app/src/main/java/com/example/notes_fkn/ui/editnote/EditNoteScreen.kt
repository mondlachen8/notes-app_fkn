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
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.notes_fkn.model.Note

data class TextSpan(
    val start: Int,
    val end: Int,
    val bold: Boolean = false,
    val italic: Boolean = false
)

@Composable
fun EditNoteScreen(
    note: Note?,
    onSave: (Note) -> Unit,
    onCancel: () -> Unit
) {
    var title by rememberSaveable { mutableStateOf(note?.title ?: "") }
    //var content by rememberSaveable { mutableStateOf(note?.content ?: "") }

    var textFieldValue by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(note?.content ?: ""))
    }
    // Textgröße global
    var textSize by rememberSaveable { mutableStateOf(16f) }

    // Fett-/Kursiv-Spans
    var spans by remember { mutableStateOf(listOf<TextSpan>())}

    // Fett/Kursiv Status für gesamten Text (vereinfacht)
    var isBold by rememberSaveable { mutableStateOf(false) }
    var isItalic by rememberSaveable { mutableStateOf(false) }
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
                    /**val editedNote = note?.copy(
                        title = title,
                        content = content
                    ) ?: Note(
                        id = System.currentTimeMillis(),
                        title = title,
                        content = content
                    )
                    onSave(editedNote)*/
                    val annotatedText = buildAnnotatedString {
                        append(textFieldValue.text)
                        spans.forEach { span ->
                            addStyle(
                                SpanStyle(
                                    fontWeight = if (span.bold) FontWeight.Bold else null,
                                    fontStyle = if (span.italic) FontStyle.Italic else null
                                ),
                                span.start,
                                span.end
                            )
                        }
                    }
                    val editedNote = note?.copy(
                        title = title,
                        content = annotatedText.text
                    ) ?: Note(
                        id = System.currentTimeMillis(),
                        title = title,
                        content = annotatedText.text
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
                .padding(WindowInsets.statusBars.asPaddingValues())
                .padding(16.dp)
                .fillMaxSize()
        ) {

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Titel") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))
            Text("Textgröße: ${textSize.sp.value.toInt()}sp")
            Slider(
                value = textSize.sp.value,
                onValueChange = { textSize = it },
                valueRange = 12f..36f
            )

            // Fett / Kursiv Buttons
            Row {
                /**IconButton(onClick = { isBold = !isBold }) {
                Icon(
                imageVector = Icons.Filled.FormatBold,
                contentDescription = "Fett"
                )
                }*/
                IconButton(onClick = {
                    val selection = textFieldValue.selection
                    if (!selection.collapsed) {
                        spans = spans + TextSpan(
                            start = selection.start,
                            end = selection.end,
                            bold = !isBold
                        )
                    }
                }){
                    Icon(Icons.Default.FormatBold, contentDescription = "Fett")
                }
                /**IconButton(onClick = { isItalic = !isItalic }) {
                Icon(
                imageVector = Icons.Filled.FormatItalic,
                contentDescription = "Kursiv"
                )
                }*/
                IconButton(onClick = {
                    val selection = textFieldValue.selection
                    if (!selection.collapsed) {
                        spans = spans + TextSpan(
                            start = selection.start,
                            end = selection.end,
                            italic = !isItalic
                        )
                    }
                }) {
                    Icon(Icons.Default.FormatItalic, contentDescription = "Kursiv")
                }
            }

            OutlinedTextField(
                //value = content,
                value = textFieldValue,
                //onValueChange = { content = it },
                onValueChange = { textFieldValue = it},
                label = { Text("Inhalt") },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                textStyle = TextStyle(fontSize = textSize.sp),
                maxLines = Int.MAX_VALUE
            )
            /**
            Row {
                IconButton(onClick = { applyStyle(Style.BOLD) }) {
                    Icon(Icons.Default.FormatBold, contentDescription = "Fett")
                }
                IconButton(onClick = { applyStyle(Style.ITALIC) }) {
                    Icon(Icons.Default.FormatItalic, contentDescription = "Kursiv")
                }
            }*/

            // Vorschau mit angewendeten Spans
            Spacer(modifier = Modifier.height(12.dp))
            Text("Vorschau:")
            val annotatedPreview = buildAnnotatedString {
                append(textFieldValue.text)
                spans.forEach { span ->
                    addStyle(
                        SpanStyle(
                            fontWeight = if (span.bold) FontWeight.Bold else null,
                            fontStyle = if (span.italic) FontStyle.Italic else null,
                            fontSize = textSize.sp,
                        ),
                        span.start,
                        span.end
                    )
                }
            }
            Text(
                annotatedPreview,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
        }
    }
}

enum class Style { BOLD, ITALIC }
