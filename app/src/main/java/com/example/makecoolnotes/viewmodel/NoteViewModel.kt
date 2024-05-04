package com.example.makecoolnotes.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Query
import com.example.makecoolnotes.model.NoteModel
import com.example.makecoolnotes.repository.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModel(private val repository: NoteRepository):ViewModel() {


    fun saveNote(note:NoteModel)=viewModelScope.launch(Dispatchers.IO) {
        repository.addNote(note)
    }

    fun deleteNote(note:NoteModel)=viewModelScope.launch(Dispatchers.IO) {
        repository.deleteNote(note)
    }

    fun updateNote(note:NoteModel)=viewModelScope.launch(Dispatchers.IO) {
        repository.updateNote(note)
    }

    fun searchNote(query:String):LiveData<List<NoteModel>>{
        return repository.searchNote(query)
    }

    fun getNotes():LiveData<List<NoteModel>> =repository.getAllNotes()

}