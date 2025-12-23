package com.example.notes_fkn.data

import android.content.Context
import com.example.notes_fkn.model.Note
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

class NotesFileRepository(
    private val context: Context
) {

    private val gson = Gson()
    private val fileName = "notes.json"

    private fun getFile(): File =
        File(context.filesDir, fileName)

    fun loadNotes(): List<Note> {
        val file = getFile()
        if (!file.exists()) return emptyList()

        val json = file.readText()
        val type = object : TypeToken<List<Note>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }

    fun saveNotes(notes: List<Note>) {
        val json = gson.toJson(notes)
        getFile().writeText(json)
    }
}
