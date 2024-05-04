package com.example.makecoolnotes.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.makecoolnotes.model.NoteModel


@Dao
interface DaoInterface {

    @Insert
    suspend fun addNote(note:NoteModel)

    @Update
    suspend fun updateNote(note:NoteModel)

    @Query("SELECT * FROM NoteModel ORDER BY id DESC")
    fun getAllNotes():LiveData<List<NoteModel>>

    @Query("SELECT * FROM NoteModel WHERE title LIKE : query OR date LIKE:query ORDER BY id DESC")
    fun search(query:String):LiveData<List<NoteModel>>

    @Delete
    suspend fun deleteNote(note:NoteModel)

}