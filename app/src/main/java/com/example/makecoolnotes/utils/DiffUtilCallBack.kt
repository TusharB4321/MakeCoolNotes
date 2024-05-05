package com.example.makecoolnotes.utils

import androidx.recyclerview.widget.DiffUtil
import com.example.makecoolnotes.model.NoteModel

class DiffUtilCallBack:DiffUtil.ItemCallback<NoteModel>() {
    override fun areItemsTheSame(oldItem: NoteModel, newItem: NoteModel): Boolean {
        return oldItem.id==newItem.id
    }

    override fun areContentsTheSame(oldItem: NoteModel, newItem: NoteModel): Boolean {
        return oldItem.id==newItem.id
    }

}