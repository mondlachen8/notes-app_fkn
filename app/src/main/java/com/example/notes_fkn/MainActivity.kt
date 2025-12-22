package com.example.notes_fkn

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.notes_fkn.ui.NotesApp
import com.example.notes_fkn.ui.theme.Notes_fknTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Notes_fknTheme {
                NotesApp()
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Notes_fknTheme {
        NotesApp()
    }
}
