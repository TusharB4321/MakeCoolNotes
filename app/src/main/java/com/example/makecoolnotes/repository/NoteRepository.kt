package com.example.makecoolnotes.repository

import androidx.room.Query
import com.example.makecoolnotes.dao.NoteDatabase
import com.example.makecoolnotes.model.NoteModel

class NoteRepository(private val db:NoteDatabase) {

    fun getAllNotes()=db.getNoteDao().getAllNotes()

    fun searchNote(query: String)=db.getNoteDao().search(query)

    suspend fun addNote(note:NoteModel)=db.getNoteDao().addNote(note)

    suspend fun updateNote(note:NoteModel)=db.getNoteDao().updateNote(note)

    suspend fun deleteNote(note:NoteModel)=db.getNoteDao().deleteNote(note)


}