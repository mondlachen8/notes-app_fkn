package com.example.notes_fkn.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.notes_fkn.model.NotesViewModel
import com.example.notes_fkn.ui.editnote.EditNoteScreen
import com.example.notes_fkn.ui.navigation.Routes
import com.example.notes_fkn.ui.notedetail.NoteDetailScreen
import com.example.notes_fkn.ui.noteslist.NotesList

@Composable
fun NotesApp() {

    val navController = rememberNavController()
    //var notes by remember { mutableStateOf(listOf<Note>()) }
    //val viewModel: NotesViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
    val viewModel: NotesViewModel = viewModel()
    val notes by viewModel.notes.collectAsState()


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
                    //navController.navigate("${Routes.EDIT_NOTE}/${note.id}")
                    navController.navigate("${Routes.NOTE_DETAIL}/${note.id}")
                }
            )
        }

        composable(
            route = "${Routes.NOTE_DETAIL}/{noteId}",
            arguments = listOf(
                navArgument("noteId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val noteId = backStackEntry.arguments?.getLong("noteId")
            val note = noteId?.let { viewModel.getNoteById(it) }
            if (note != null) {
                NoteDetailScreen(
                    note = note, // must not be null
                    onBack = { navController.popBackStack() },
                    onEdit = {
                        navController.navigate("${Routes.EDIT_NOTE}/${noteId}")
                    },
                    onDeleteConfirmed = {
                        viewModel.deleteNote(noteId)
                        navController.navigate(Routes.NOTES_LIST){
                            popUpTo(Routes.NOTES_LIST) {
                                inclusive = true
                            }
                        }
                    }
                )
            } else{
                //Note existiert nicht mehr
                LaunchedEffect(Unit) {
                    navController.navigate(Routes.NOTES_LIST) {
                        popUpTo(Routes.NOTES_LIST){inclusive = true}
                    }}
            }
        }

        composable(Routes.EDIT_NOTE) {
            EditNoteScreen(
                note = null,
                onSave = { savedNote ->
                    viewModel.saveNote(savedNote)
                    navController.navigate("${Routes.NOTE_DETAIL}/${savedNote.id}") {
                        popUpTo(Routes.NOTES_LIST)
                    }
                    //navController.popBackStack()
                },
                onCancel = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = "${Routes.EDIT_NOTE}/{noteId}",
            arguments = listOf(
                navArgument("noteId") {
                    type = NavType.LongType
                }
            )
        ) {backStackEntry ->
            val noteId = backStackEntry.arguments?.getLong("noteId")
            //val note = noteId?.let { viewModel.getNoteById(it) }
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

