package com.example.makecoolnotes

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.room.RoomDatabase
import com.example.makecoolnotes.dao.NoteDatabase
import com.example.makecoolnotes.databinding.BottomSheetLayoutBinding
import com.example.makecoolnotes.databinding.FragmentSaveOrDeleteBinding
import com.example.makecoolnotes.model.NoteModel
import com.example.makecoolnotes.repository.NoteRepository
import com.example.makecoolnotes.utils.hideKeyBoard
import com.example.makecoolnotes.viewmodel.NoteViewModel
import com.example.makecoolnotes.viewmodel.NoteViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.transition.MaterialContainerTransform
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.text.SimpleDateFormat
import java.util.*

class SaveOrDeleteFragment : Fragment(R.layout.fragment_save_or_delete) {

    private lateinit var navController: NavController
    private lateinit var binding: FragmentSaveOrDeleteBinding
    private var note:NoteModel?=null
    private var color=-1
    private  val viewModel:NoteViewModel by activityViewModels()
    private val currentDateTime=SimpleDateFormat("d MMM yyyy hh:mm:ss a", Locale.getDefault()).format(Date())
    private val job= CoroutineScope(Dispatchers.Main)
    private val args:SaveOrDeleteFragmentArgs by navArgs()
    private lateinit var result:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val animation=MaterialContainerTransform().apply {
            drawingViewId=R.id.fragment
            scrimColor= Color.TRANSPARENT
            duration=300L
        }

        sharedElementEnterTransition=animation
        sharedElementReturnTransition=animation
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding= FragmentSaveOrDeleteBinding.bind(view)
        navController=Navigation.findNavController(view)
        val activity=activity as MainActivity
        binding.backBtn.setOnClickListener {
            requireView().hideKeyBoard()
            navController.popBackStack()
        }

        binding.lastEdited.text=getString(R.string.edited_on,SimpleDateFormat.getDateInstance().format(
            Date()
        ))
        binding.saveNote.setOnClickListener {
            saveNote()
        }

        try {
            binding.etNoteContent.setOnFocusChangeListener{_,hasFocus->

                if (hasFocus){
                    binding.bottomBar.visibility=View.VISIBLE
                    binding.etNoteContent.setStylesBar(binding.styleBar)
                }else{
                    binding.bottomBar.visibility=View.INVISIBLE
                }

            }
        }catch (e:Throwable){
            Log.d("Tag",e.stackTraceToString())
        }

        binding.fabColorPick.setOnClickListener {

            val bottomSheetDialog=BottomSheetDialog(
                requireContext(),
                R.style.BottomSheetDialogTheme
            )

            val bottomSheetView:View=layoutInflater.inflate(
                R.layout.bottom_sheet_layout,
                null
            )

            with(bottomSheetDialog){
                setContentView(bottomSheetView)
                show()
            }
            val bottomSheetBinding=BottomSheetLayoutBinding.bind(bottomSheetView)

            bottomSheetBinding.apply {
                colorPicker.apply {
                    setSelectedColor(color)
                    setOnColorSelectedListener {
                        value->
                        color=value

                        binding.apply {
                            noteContentFragParent.setBackgroundColor(color)
                            toolbarFragNoteContent.setBackgroundColor(color)
                            bottomBar.setBackgroundColor(color)
                            activity.window.statusBarColor=color

                        }
                        bottomSheetBinding.bottomSheetParent.setCardBackgroundColor(color)

                    }
                }
                bottomSheetParent.setCardBackgroundColor(color)
            }

            bottomSheetView.post{
                bottomSheetDialog.behavior.state=BottomSheetBehavior.STATE_EXPANDED
            }

        }

    }

    private fun saveNote() {

        if (binding.etNoteContent.toString().isEmpty()||
                binding.etTitle.toString().isEmpty()){
            Toast.makeText(requireContext(), "Please Fill all fields", Toast.LENGTH_SHORT).show()
        }else{

            note=args.note  // we check it is new note or updated note

            Log.d("CheckError","Text is: ${binding.etTitle.text}")
            when(note) {
                null -> {
                    viewModel.saveNote(
                        NoteModel(
                            0,
                            binding.etTitle.text.toString(),
                            binding.etNoteContent.getMD(),
                            date = currentDateTime,
                            color
                        )
                    )

                    result="Note Saved"
                    setFragmentResult(
                        "key",
                        bundleOf("bundleKey" to result)
                    )
                    navController.navigate(SaveOrDeleteFragmentDirections.actionSaveOrDeleteFragmentToNoteFragment())
                }else->{
                    //Update note
                }

            }


        }
    }

}