package com.example.notes_fkn.ui.editnote

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material.icons.filled.FormatBold
import androidx.compose.material.icons.filled.FormatItalic
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.util.copy
import com.example.notes_fkn.model.Note
import com.example.notes_fkn.model.TextSpan
import com.example.notes_fkn.model.hasStyleInSelection
import com.example.notes_fkn.model.removeStyleFromSelection

@Composable
fun EditNoteScreen(
    note: Note?,
    onSave: (Note) -> Unit,
    onCancel: () -> Unit
) {
    var title by rememberSaveable { mutableStateOf(note?.title ?: "") }
    var content by rememberSaveable { mutableStateOf(note?.content ?: "") }

    var textFieldValue by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(content))
    }
    // Textgröße global
    var textSize by rememberSaveable { mutableStateOf(16f) }

    // Fett-/Kursiv-Spans
    var spans by remember { mutableStateOf(listOf<TextSpan>())}

    // Fett/Kursiv Status für gesamten Text (vereinfacht)
    var isBold by rememberSaveable { mutableStateOf(false) }
    var isItalic by rememberSaveable { mutableStateOf(false) }
    /**fun applyStyle(style: Style) {
        val start = textFieldValue.selection.start
        val end = textFieldValue.selection.end

        if (start == end) return // keine Auswahl

        val builder = AnnotatedString.Builder(textFieldValue.text)
        when (style) {
            Style.BOLD -> builder.addStyle(SpanStyle(fontWeight = FontWeight.Bold), start, end)
            Style.ITALIC -> builder.addStyle(SpanStyle(fontStyle = FontStyle.Italic), start, end)
        }

        textFieldValue = textFieldValue.copy(text = builder.toAnnotatedString().text)
    }*/
    fun applyStyleToSelection(
        bold: Boolean = false,
        italic: Boolean = false,
        fontSize: Float = 16f
    ) {
        val start = textFieldValue.selection.start
        val end = textFieldValue.selection.end

        if (start == end) return

        spans = spans + TextSpan(
            start = start,
            end = end,
            bold = bold,
            italic = italic,
            fontSize = fontSize
        )
    }
    fun toggleStyle(
        style: Style
    ) {
        val start = textFieldValue.selection.start
        val end = textFieldValue.selection.end

        if (start == end) return

        val isAlreadyStyled = hasStyleInSelection(
            spans = spans,
            start = start,
            end = end
        ) { span ->
            when (style) {
                Style.BOLD -> span.bold
                Style.ITALIC -> span.italic
            }
        }

        spans = if (isAlreadyStyled) {
            removeStyleFromSelection(
                spans = spans,
                start = start,
                end = end
            ) { span ->
                when (style) {
                    Style.BOLD -> span.bold
                    Style.ITALIC -> span.italic
                }
            }
        } else {
            spans + TextSpan(
                start = start,
                end = end,
                bold = style == Style.BOLD,
                italic = style == Style.ITALIC
            )
        }
    }
    fun toggleTodoAtCursor() {
        val start = textFieldValue.selection.start
        val text = textFieldValue.text

        // Zeilenanfang der aktuellen Zeile finden
        val lineStart = text.lastIndexOf('\n', start - 1).let { if (it == -1) 0 else it + 1 }
        val lineEnd = text.indexOf('\n', start).let { if (it == -1) text.length else it }

        val lineText = text.substring(lineStart, lineEnd)

        val newLineText = if (lineText.trimStart().startsWith("[ ] ")) {
            // TODO schon vorhanden → entfernen
            lineText.replaceFirst("\\[ \\] ".toRegex(), "")
        } else {
            // TODO einfügen
            "  [ ] $lineText"
        }

        // Text ersetzen
        val newText = text.replaceRange(lineStart, lineEnd, newLineText)
        textFieldValue = textFieldValue.copy(
            text = newText,
            selection = TextRange(
                start = lineStart + newLineText.length,
                end = lineStart + newLineText.length
            )
        )
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
                        content = annotatedText.text,
                        spans = spans
                    ) ?: Note(
                        id = System.currentTimeMillis(),
                        title = title,
                        content = annotatedText.text,
                        spans = spans
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
                IconButton(onClick = {
                    isBold = !isBold
                    //applyStyleToSelection(bold = isBold)
                    toggleStyle(Style.BOLD)
                }){
                    Icon(Icons.Default.FormatBold, contentDescription = "Fett")
                }
                IconButton(onClick = {
                    isItalic = !isItalic
                    //applyStyleToSelection(italic = isItalic)
                    toggleStyle(Style.ITALIC)
                }) {
                    Icon(Icons.Default.FormatItalic, contentDescription = "Kursiv")
                }
                IconButton(onClick = { toggleTodoAtCursor() }) {
                    Icon(Icons.Default.CheckBoxOutlineBlank, contentDescription = "TODO")
                }
            }
            val scrollState = rememberScrollState()
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {

                OutlinedTextField(
                    //value = content,
                    value = textFieldValue,
                    //onValueChange = { content = it },
                    //onValueChange = { textFieldValue = it},
                    onValueChange = { newValue ->
                        content = newValue.text
                        textFieldValue = newValue
                    },
                    label = { Text("Inhalt") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .verticalScroll(scrollState),
                    textStyle = TextStyle(fontSize = textSize.sp),
                    maxLines = Int.MAX_VALUE
                )

                // Vorschau mit angewendeten Spans
                Spacer(modifier = Modifier.height(12.dp))
                Text("Vorschau:")
                Box (
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .verticalScroll(scrollState)
                        .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                        .padding(8.dp)
                ) {
                    Text(
                        //annotatedPreview,
                        text = buildAnnotatedContent(content, spans),
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                }
            }
        }
    }
}

enum class Style { BOLD, ITALIC }

fun buildAnnotatedContent(
    text: String,
    spans: List<TextSpan>
): AnnotatedString {
    val builder = AnnotatedString.Builder(text)

    spans.forEach { span ->
        builder.addStyle(
            SpanStyle(
                fontWeight = if (span.bold) FontWeight.Bold else null,
                fontStyle = if (span.italic) FontStyle.Italic else null,
                fontSize = span.fontSize.sp
            ),
            span.start,
            span.end
        )
    }

    return builder.toAnnotatedString()
}