package com.example.notes_fkn.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.notes_fkn.model.NotesViewModel
import com.example.notes_fkn.ui.editnote.EditNoteScreen
import com.example.notes_fkn.ui.navigation.Routes
import com.example.notes_fkn.ui.noteslist.NotesList

@Composable
fun NotesApp() {

    val navController = rememberNavController()
    //var notes by remember { mutableStateOf(listOf<Note>()) }
    val viewModel: NotesViewModel = androidx.lifecycle.viewmodel.compose.viewModel()


    NavHost(
        navController = navController,
        startDestination = Routes.NOTES_LIST
    ) {

        composable(Routes.NOTES_LIST) {
            val notes by viewModel.notes.collectAsState()
            NotesList(
                notes = notes,
                onAddNote = {
                    navController.navigate(Routes.EDIT_NOTE)
                },
                onNoteClick = { note ->
                    navController.navigate("${Routes.EDIT_NOTE}/${note.id}")
                }
            )
        }

        composable(
            route = "${Routes.EDIT_NOTE}/{noteId}",
            arguments = listOf(
                navArgument("noteId") {
                    type = NavType.LongType
                    nullable = true
                }
            )
        ) {backStackEntry ->
            val noteId = backStackEntry.arguments?.getLong("noteId")
            val note = noteId?.let { viewModel.getNoteById(it) }

            EditNoteScreen(
                note = note, // kommt im nächsten Schritt aus ViewModel
                /**onSave = {
                    navController.popBackStack()
                },*/
                onSave = { savedNote ->
                    viewModel.saveNote(savedNote)
                    navController.popBackStack()
                },
                onCancel = {
                    navController.popBackStack()
                }
            )
        }
    }
}

