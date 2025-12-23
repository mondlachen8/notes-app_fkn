package com.example.notes_fkn.model

data class TextSpan(
    val start: Int,
    val end: Int,
    val bold: Boolean = false,
    val italic: Boolean = false,
    val fontSize: Float = 16f
)

fun hasStyleInSelection(
    spans: List<TextSpan>,
    start: Int,
    end: Int,
    predicate: (TextSpan) -> Boolean
): Boolean {
    return spans.any { span ->
        predicate(span) &&
                span.start < end &&
                span.end > start
    }
}

fun removeStyleFromSelection(
    spans: List<TextSpan>,
    start: Int,
    end: Int,
    predicate: (TextSpan) -> Boolean
): List<TextSpan> {
    return spans.filterNot { span ->
        predicate(span) &&
                span.start < end &&
                span.end > start
    }
}