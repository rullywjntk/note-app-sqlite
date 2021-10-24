package com.rully.noteappsq.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rully.noteappsq.R
import com.rully.noteappsq.databinding.ItemNoteBinding
import com.rully.noteappsq.entity.Note

class NoteAdapter(private val onItemClickCallback: OnItemClickCallback) :
    RecyclerView.Adapter<NoteAdapter.ViewHolder>() {

    var listNote = ArrayList<Note>()
        set(listNote) {
            if (listNote.size > 0) {
                this.listNote.clear()
            }
            this.listNote.addAll(listNote)
        }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemNoteBinding.bind(itemView)
        fun bind(note: Note) {
            binding.tvTitle.text = note.title
            binding.tvDate.text = note.date
            binding.tvDesc.text = note.description
            binding.cvItem.setOnClickListener {
                onItemClickCallback.onItemClicked(note, adapterPosition)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listNote[position])
    }

    override fun getItemCount(): Int = this.listNote.size

    fun addItem(note: Note) {
        this.listNote.add(note)
        notifyItemInserted(this.listNote.size - 1)
    }

    fun updateItem(position: Int, note: Note) {
        this.listNote[position] = note
        notifyItemChanged(position, note)
    }

    fun deleteItem(position: Int) {
        this.listNote.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, this.listNote.size)
    }

    interface OnItemClickCallback {
        fun onItemClicked(selectedNote: Note, position: Int)
    }

}