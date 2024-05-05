package com.example.makecoolnotes

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.makecoolnotes.dao.NoteDatabase
import com.example.makecoolnotes.databinding.ActivityMainBinding
import com.example.makecoolnotes.repository.NoteRepository
import com.example.makecoolnotes.viewmodel.NoteViewModel
import com.example.makecoolnotes.viewmodel.NoteViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding
    lateinit var viewModel: NoteViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
       supportActionBar?.hide()

        try {
            setContentView(binding.root)
            val noteRepository=NoteRepository(NoteDatabase(this))
            val noteViewModelFactory=NoteViewModelFactory(noteRepository)
            viewModel=ViewModelProvider(this,noteViewModelFactory)[NoteViewModel::class.java]

        }catch (e:Exception){
            Log.d("Tag",e.message.toString())
        }

    }
}