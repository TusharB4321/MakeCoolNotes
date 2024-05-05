package com.example.makecoolnotes

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.example.makecoolnotes.databinding.FragmentNoteBinding
import com.example.makecoolnotes.utils.hideKeyBoard
import com.example.makecoolnotes.viewmodel.NoteViewModel
import com.google.android.material.transition.MaterialElevationScale
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class NoteFragment : Fragment(R.layout.fragment_note) {

    lateinit var binding: FragmentNoteBinding
   // private val viewmodel:NoteViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        exitTransition=MaterialElevationScale(false).apply {
            duration=350
        }

        enterTransition=MaterialElevationScale(false).apply {
            duration=350
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding= FragmentNoteBinding.bind(view)
        val activity=activity as MainActivity
        requireView().hideKeyBoard()

        CoroutineScope(Dispatchers.Main).launch{
            delay(10)
            //activity.window.statusBarColor= Color.WHITE
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            activity.window.statusBarColor=Color.parseColor("#9E9D9D")

        }

      onit()

    }

    private fun onit() {
        val navController= view?.let { Navigation.findNavController(it) }
        binding.addNoteFb.setOnClickListener {
            binding.appBarLayout.visibility=View.INVISIBLE
            navController?.navigate(NoteFragmentDirections.actionNoteFragmentToSaveOrDeleteFragment())

        }

        binding.innerFb.setOnClickListener {
            binding.appBarLayout.visibility=View.INVISIBLE
            navController?.navigate(NoteFragmentDirections.actionNoteFragmentToSaveOrDeleteFragment())

        }
    }
}