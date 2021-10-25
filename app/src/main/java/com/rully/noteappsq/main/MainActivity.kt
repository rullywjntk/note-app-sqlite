package com.rully.noteappsq.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.rully.noteappsq.R
import com.rully.noteappsq.adapter.NoteAdapter
import com.rully.noteappsq.databinding.ActivityMainBinding
import com.rully.noteappsq.db.NoteHelper
import com.rully.noteappsq.entity.Note
import com.rully.noteappsq.utils.MappingHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var noteAdapter: NoteAdapter

    val resultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.data != null) {
            when (result.resultCode) {
                NoteActivity.RESULT_ADD -> {
                    val note =
                        result.data?.getParcelableExtra<Note>(NoteActivity.EXTRA_NOTE) as Note
                    noteAdapter.addItem(note)
                    binding.rvNote.smoothScrollToPosition(noteAdapter.itemCount - 1)
                    showSnackBarMessage("Item added successfully")
                }
                NoteActivity.RESULT_UPDATE -> {
                    val note =
                        result.data?.getParcelableExtra<Note>(NoteActivity.EXTRA_NOTE) as Note
                    val position = result?.data?.getIntExtra(NoteActivity.EXTRA_POSITION, 0) as Int
                    noteAdapter.updateItem(position, note)
                    binding.rvNote.smoothScrollToPosition(position)
                    showSnackBarMessage("Item changed successfully")
                }
                NoteActivity.RESULT_DELETE -> {
                    val position = result?.data?.getIntExtra(NoteActivity.EXTRA_POSITION, 0) as Int
                    noteAdapter.deleteItem(position)
                    showSnackBarMessage("Item deleted successfully")
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = getString(R.string.app_name)

        binding.rvNote.layoutManager = LinearLayoutManager(this)
        binding.rvNote.setHasFixedSize(true)

        val intent = Intent(this@MainActivity, NoteActivity::class.java)

        noteAdapter = NoteAdapter(object : NoteAdapter.OnItemClickCallback {
            override fun onItemClicked(selectedNote: Note?, position: Int?) {
                intent.putExtra(NoteActivity.EXTRA_NOTE, selectedNote)
                intent.putExtra(NoteActivity.EXTRA_POSITION, position)
                resultLauncher.launch(intent)
            }
        })
        binding.rvNote.adapter = noteAdapter
        binding.fabAdd.setOnClickListener {
            resultLauncher.launch(intent)
        }

        if (savedInstanceState == null) {
            loadData()
        } else {
            val list = savedInstanceState.getParcelableArrayList<Note>(EXTRA_STATE)
            if (list != null) {
                noteAdapter.listNote = list
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putParcelableArrayList(EXTRA_STATE, noteAdapter.listNote)
    }

    private fun loadData() {
        lifecycleScope.launch {
            binding.progressBar.visibility = View.VISIBLE
            val noteHelper = NoteHelper.getInstance(applicationContext)
            noteHelper.open()
            val deferredNotes = async(Dispatchers.IO) {
                val cursor = noteHelper.queryAll()
                MappingHelper.mapCursorToArrayList(cursor)
            }
            binding.progressBar.visibility = View.INVISIBLE
            val note = deferredNotes.await()
            if (note.size > 0) {
                noteAdapter.listNote = note
            } else {
                noteAdapter.listNote = ArrayList()
                showSnackBarMessage("There is no data right now")
            }
            noteHelper.close()
        }
    }

    private fun showSnackBarMessage(message: String) {
        Snackbar.make(binding.rvNote, message, Snackbar.LENGTH_SHORT).show()
    }

    companion object {
        private const val EXTRA_STATE = "extra_state"
    }
}