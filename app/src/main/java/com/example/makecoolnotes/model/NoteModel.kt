package com.example.makecoolnotes.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class NoteModel(
    @PrimaryKey(autoGenerate = true)
    val id:Int=0,
    val title:String,
    val content:String,
    val date:String,
    val color:Int=-1
):Serializable       //if we want to pass the entire object between the fragment then serializable used
