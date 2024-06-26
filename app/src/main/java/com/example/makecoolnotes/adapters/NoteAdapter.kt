package com.example.makecoolnotes.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.makecoolnotes.R
import com.example.makecoolnotes.databinding.NoteItemLayoutBinding
import com.example.makecoolnotes.model.NoteModel
import com.example.makecoolnotes.utils.DiffUtilCallBack
import com.example.makecoolnotes.viewmodel.NoteViewModel
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textview.MaterialTextView
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.Markwon
import io.noties.markwon.MarkwonVisitor
import io.noties.markwon.ext.strikethrough.StrikethroughPlugin
import io.noties.markwon.ext.tasklist.TaskListPlugin
import org.commonmark.node.SoftLineBreak

class NoteAdapter:ListAdapter<NoteModel,NoteAdapter.NoteViewHolder>(DiffUtilCallBack()){

    inner class NoteViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        private val binding=NoteItemLayoutBinding.bind(itemView)

        val title:MaterialTextView=binding.noteItemTitle
        val content:TextView=binding.noteContainItem
        val date:MaterialTextView=binding.noteDate
        val parent:MaterialCardView=binding.noteItemLayParent
        val markwon=Markwon.builder(itemView.context)
            .usePlugin(StrikethroughPlugin.create())
            .usePlugin(TaskListPlugin.create(itemView.context))
            .usePlugin(object:AbstractMarkwonPlugin(){
                override fun configureVisitor(builder: MarkwonVisitor.Builder) {
                    super.configureVisitor(builder)

                    builder.on(
                        SoftLineBreak::class.java,
                    ){visitor,_->visitor.forceNewLine()}
                }
            }).build()


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteAdapter.NoteViewHolder {
       return NoteViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.note_item_layout,parent,false))
    }

    override fun onBindViewHolder(holder: NoteAdapter.NoteViewHolder, position: Int) {
        getItem(position).let {note->
            holder.apply {
                title.text=note.title
                markwon.setMarkdown(content,note.content)
                date.text=note.date
                parent.setCardBackgroundColor(note.color)

                itemView.setOnClickListener {

                }

                content.setOnClickListener {

                }
            }

        }
    }
}