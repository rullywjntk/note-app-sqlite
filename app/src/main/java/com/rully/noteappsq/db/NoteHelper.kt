package com.rully.noteappsq.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.rully.noteappsq.db.DatabaseContract.NoteColumns.Companion.ID
import com.rully.noteappsq.db.DatabaseContract.NoteColumns.Companion.TABLE_NAME
import java.sql.SQLException
import kotlin.jvm.Throws

class NoteHelper(context: Context) {

    private var databaseHelper: DatabaseHelper = DatabaseHelper(context)
    private lateinit var database: SQLiteDatabase

    /**
     *  Metode membuka koneksi database
     */
    @Throws(SQLException::class)
    fun open() {
        database = databaseHelper.writableDatabase
    }

    /**
     *  Metode menutup koneksi database
     */
    fun close() {
        databaseHelper.close()

        if (database.isOpen)
            database.close()
    }

    /**
     *  Metode mengambil semua data
     */
    fun queryAll(): Cursor {
        return database.query(DATABASE_TABLE, null, null, null, null, null, "$ID ASC")
    }

    /**
     *  Metode mengambil data berdasarkan ID
     */
    fun query(id: String): Cursor {
        return database.query(DATABASE_TABLE, null, "$ID = ?", arrayOf(id), null, null, null, null)
    }

    /**
     *  Metode menyimpan data
     */
    fun insert(values: ContentValues?): Long {
        return database.insert(DATABASE_TABLE, null, values)
    }

    /**
     *  Metode memperbarui data
     */
    fun update(id: String, values: ContentValues?): Int {
        return database.update(DATABASE_TABLE, values, "$ID = ?", arrayOf(id))
    }

    /**
     *  Metode menghapus data
     */
    fun deleteById(id: String): Int {
        return database.delete(DATABASE_TABLE, "$ID = '$id'", null)
    }


    companion object {
        private const val DATABASE_TABLE = TABLE_NAME

        /**
         *  Metode menginisiasi database
         */
        private var INSTANCE: NoteHelper? = null
        fun getInstance(context: Context): NoteHelper = INSTANCE ?: synchronized(this) {
            INSTANCE ?: NoteHelper(context)
        }

    }
}