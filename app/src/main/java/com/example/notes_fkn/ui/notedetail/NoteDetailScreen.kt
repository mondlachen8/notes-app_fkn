package com.example.notes_fkn.ui.notedetail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.notes_fkn.model.Note
import com.example.notes_fkn.ui.components.NoteDetailTopBar
import com.example.notes_fkn.ui.editnote.buildAnnotatedContent
import com.example.notes_fkn.model.TextSpan

@Composable
fun NoteDetailScreen(
    note: Note,
    onBack: (updatedContent: String) -> Unit,
    onEdit: (updatedContent: String) -> Unit,
    onDeleteConfirmed: () -> Unit
    //onTodoClick: (lineIndex: Int) -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    //val annotated = buildTodoAnnotatedString(note.content, note.spans)
    var content by remember(note.id) {
        mutableStateOf(note.content)
    }
    val annotated = remember(content, note.spans) {
        buildTodoAnnotatedString(content, note.spans)
    }

    var textLayoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }


    Scaffold(
        topBar = {
            NoteDetailTopBar(
                title = note.title,
                onBack = {onBack(content)},
                onEdit = {onEdit(content)},
                //onDelete = onDeleteConfirmed
                onDelete = if (note.isPinned) null else {{
                        showDeleteDialog = true
                    }}
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .pointerInput(annotated) {
                        detectTapGestures { tapPosition ->
                            textLayoutResult?.let { layout ->
                                val rawOffset = layout.getOffsetForPosition(tapPosition)
                                val offset = rawOffset.coerceIn(0, annotated.length)

                                val annotations = annotated.getStringAnnotations(
                                    tag = "TODO",
                                    start = offset,
                                    end = offset
                                )

                                if (annotations.isNotEmpty()) {
                                    val lineIndex = annotations.first().item.toInt()
                                    content = toggleTodoInContent(
                                        content = content,
                                        lineIndex = lineIndex
                                    )
                                    //onTodoClick(annotations.first().item.toInt())
                                }
                            }
                        }
                    }
            ) {
                Text(
                    //text = buildAnnotatedContent(note.content, note.spans),
                    text = annotated,
                    style = MaterialTheme.typography.bodyLarge,
                    onTextLayout = { result ->
                        textLayoutResult = result
                    }
                )
            }
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

fun buildTodoAnnotatedString(
    content: String,
    spans: List<TextSpan>): AnnotatedString {
    return buildAnnotatedString {
        /**content.lines().forEachIndexed { index, line ->
            val trimmed = line.trimStart()

            // TODO-Zeile erkennen
            if (trimmed.startsWith("[ ]") || trimmed.startsWith("[x]")) {
                pushStringAnnotation(tag = "TODO", annotation = index.toString())
                withStyle(style = SpanStyle(fontWeight = FontWeight.Normal, fontSize = 16.sp)) {
                    append(line)
                }
                pop()
            } else {
                append(line)
            }
            append("\n")
        }*/
        val lines = content.split("\n")
        lines.forEachIndexed{ index, line ->
            val trimmed = line.trimStart()
            val isTodo = trimmed.startsWith("[ ]") ||
                        trimmed.startsWith("[x]")
            val start = length

            append(line)

            val end = length

            if (isTodo) {
                addStringAnnotation(
                    tag = "TODO",
                    annotation = index.toString(),
                    start = start,
                    end = end
                )
            }

            if (index < lines.lastIndex) {
                append("\n")
            }
        }
    }
}

fun toggleTodoInContent(
    content: String,
    lineIndex: Int
): String {
    val lines = content.lines().toMutableList()
    val line = lines[lineIndex]

    lines[lineIndex] = when {
        line.trimStart().startsWith("[ ]") ->
            line.replaceFirst("[ ]", "[x]")
        line.trimStart().startsWith("[x]") ->
            line.replaceFirst("[x]", "[ ]")
        else -> line
    }

    return lines.joinToString("\n")
}


