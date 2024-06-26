package com.example.makecoolnotes

import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.makecoolnotes.adapters.NoteAdapter
import com.example.makecoolnotes.databinding.FragmentNoteBinding
import com.example.makecoolnotes.utils.SwipeToDelete
import com.example.makecoolnotes.utils.hideKeyBoard
import com.example.makecoolnotes.viewmodel.NoteViewModel
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialElevationScale
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class NoteFragment : Fragment(R.layout.fragment_note) {

    lateinit var binding: FragmentNoteBinding
    private val viewmodel:NoteViewModel by activityViewModels()
    private lateinit var rvadapter: NoteAdapter
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

        recyclerViewDisplay()

        swipeToDelete(binding.rvNote)

        //Search
        binding.search.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.noData.isVisible=false
            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {

                if (s.toString().isNotEmpty()){

                    val text=s.toString()
                    val query="%$text%"

                    if (query.isNotEmpty()){
                       viewmodel.searchNote(query).observe(viewLifecycleOwner){
                           rvadapter.submitList(it)
                       }
                    }else{
                          observerDataChanges()
                    }
                }else{
                    observerDataChanges()
                }

            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        binding.search.setOnEditorActionListener { v, actionId,_ ->

            if (actionId==EditorInfo.IME_ACTION_SEARCH){
                v.clearFocus()
                requireView().hideKeyBoard()
            }
            return@setOnEditorActionListener true
        }

        binding.rvNote.setOnScrollChangeListener { view, scrollX, scrollY, i3, oldScrollY ->

            when{
                scrollY>oldScrollY->{
                    binding.chatFbText.isVisible=false
                }
                scrollX==scrollY->{
                    binding.chatFbText.isVisible=true
                }
                else->{
                    binding.chatFbText.isVisible=true
                }

            }
        }
    }

    private fun swipeToDelete(rvNote: RecyclerView) {
        val swipeTodelteCallBack=object :SwipeToDelete(){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position=viewHolder.adapterPosition
                val note=rvadapter.currentList[position]
                var actionButtonTap=false
                viewmodel.deleteNote(note)
                binding.search.apply {
                    hideKeyBoard()
                    clearFocus()
                }
                if (binding.search.text.toString().isEmpty()){
                    observerDataChanges()
                }
                val snackBar=Snackbar.make(
                    requireView(),"Note Deleted",Snackbar.LENGTH_LONG
                ).addCallback(object :BaseTransientBottomBar.BaseCallback<Snackbar>(){
                    override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                        super.onDismissed(transientBottomBar, event)

                    }

                    override fun onShown(transientBottomBar: Snackbar?) {

                        transientBottomBar?.setAction("UNDO"){
                            viewmodel.saveNote(note)
                            actionButtonTap=true
                            binding.noData.isVisible=false
                        }

                        super.onShown(transientBottomBar)
                    }
                }).apply {
                    animationMode=Snackbar.ANIMATION_MODE_FADE
                    setAnchorView(R.id.add_note_fb)
                }

                snackBar.setActionTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.yellowOrange
                    )
                )
                snackBar.show()
            }

        }

        val itemTouchHelper=ItemTouchHelper(swipeTodelteCallBack)
        itemTouchHelper.attachToRecyclerView(rvNote)
    }

    private fun observerDataChanges() {

        viewmodel.getNotes().observe(viewLifecycleOwner){list->
            binding.noData.isVisible=list.isEmpty()
            rvadapter.submitList(list)
        }
    }

    private fun recyclerViewDisplay() {

        when (resources.configuration.orientation){
            Configuration.ORIENTATION_PORTRAIT->setUpRecyclerView(2)
            Configuration.ORIENTATION_LANDSCAPE->setUpRecyclerView(3)
        }
    }

    private fun setUpRecyclerView(count: Int) {

        binding.rvNote.apply {
            layoutManager=StaggeredGridLayoutManager(count,LinearLayoutManager.VERTICAL)
            setHasFixedSize(true)
            rvadapter=NoteAdapter()
            //rvadapter.stateRestorationPolicy=RecyclerView.Adapter.St
            adapter=rvadapter

            postponeEnterTransition(300L,TimeUnit.MILLISECONDS)
            viewTreeObserver.addOnPreDrawListener {
                startPostponedEnterTransition()
                true
            }
        }
        observerDataChanges()
    }
}