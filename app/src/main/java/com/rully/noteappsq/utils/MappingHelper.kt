package com.rully.noteappsq.utils

import android.database.Cursor
import com.rully.noteappsq.db.DatabaseContract
import com.rully.noteappsq.entity.Note

object MappingHelper {

    fun mapCursorToArrayList(cursor: Cursor): ArrayList<Note> {
        val noteList = ArrayList<Note>()

        cursor.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(DatabaseContract.NoteColumns.ID))
                val title = getString(getColumnIndexOrThrow(DatabaseContract.NoteColumns.TITLE))
                val desc = getString(getColumnIndexOrThrow(DatabaseContract.NoteColumns.DESCRIPTION))
                val date = getString(getColumnIndexOrThrow(DatabaseContract.NoteColumns.DATE))
                noteList.add(Note(id, title, desc, date))
            }
        }
        return noteList
    }

}