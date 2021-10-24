package com.rully.noteappsq.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.rully.noteappsq.db.DatabaseContract.NoteColumns.Companion.TABLE_NAME

internal class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {


    override fun onCreate(p0: SQLiteDatabase) {
        p0.execSQL(SQL_CREATE_TABLE_NOTE)
    }

    override fun onUpgrade(p0: SQLiteDatabase, p1: Int, p2: Int) {
        p0.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(p0)
    }

    companion object {
        private const val DATABASE_NAME = "dbnoteapp"
        private const val DATABASE_VERSION = 1
        private const val SQL_CREATE_TABLE_NOTE = "CREATE TABLE $TABLE_NAME" +
                " (${DatabaseContract.NoteColumns.ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                " ${DatabaseContract.NoteColumns.TITLE} TEXT NOT NULL," +
                " ${DatabaseContract.NoteColumns.DESCRIPTION} TEXT NOT NULL," +
                " ${DatabaseContract.NoteColumns.DATE} TEXT NOT NULL)"
    }

}