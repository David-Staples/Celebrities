package com.example.celebrities.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.celebrities.model.Celebrity

class CelebriyDatabaseHelper(private val context: Context, private val cursorFactory: SQLiteDatabase.CursorFactory?): SQLiteOpenHelper(context, DATABASE_NAME, cursorFactory, DATABASE_VERSION) {
    override fun onCreate(database: SQLiteDatabase) {
        val createQuery = "CREATE TABLE $TABLE_NAME ($COLUMN_NAME TEXT PRIMARY KEY, $COLUMN_FAVORITE INTEGER)"
        database.execSQL(createQuery)
    }

    override fun onUpgrade(database: SQLiteDatabase, olderVersion: Int, newerVersion: Int) {
        database.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(database)
    }

    fun insertNewCelebrity(newCelebrity: Celebrity) {
        val values = ContentValues ()
        values.apply {
            put(COLUMN_NAME, newCelebrity.celebrityName)
            put(COLUMN_FAVORITE, newCelebrity.celebrityFavorite)
        }

        val database = this.writableDatabase
        database.insert(TABLE_NAME, null, values)
        database.close()
    }

    fun getAllCelebrities(): Cursor? {
        val database = this.readableDatabase

        return database.rawQuery(" SELECT * FROM $TABLE_NAME", null)
    }

//    fun getFavoriteCelebrities(): Cursor? {
//        val database = this.readableDatabase
//
//        return database.rawQuery(SELECT * FROM $TABLE_NAME WHERE )
//    }

    companion object {
        val DATABASE_NAME = "CELEBRITIES.DB"
        val DATABASE_VERSION = 1
        val TABLE_NAME = "celebrities"

        val COLUMN_NAME = "name"
        val COLUMN_FAVORITE = "favorite"

    }
}