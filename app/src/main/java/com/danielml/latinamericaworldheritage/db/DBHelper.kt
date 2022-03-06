package com.danielml.latinamericaworldheritage.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import com.danielml.latinamericaworldheritage.Model.Heritage
import java.lang.Exception

class DBHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

  companion object {
    const val DATABASE_NAME = "Heritage.db"
    const val DATABASE_VERSION = 2
    const val TABLE_HERITAGES = "heritages"
  }

  private val context = context

  override fun onCreate(dataBase: SQLiteDatabase?) {
    dataBase?.execSQL("CREATE TABLE $TABLE_HERITAGES (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, country TEXT NOT NULL, ecosystem TEXT NOT NULL)")
  }

  override fun onUpgrade(dataBase: SQLiteDatabase?, p1: Int, p2: Int) {
    dataBase?.execSQL("DROP TABLE $TABLE_HERITAGES")
    onCreate(dataBase)
  }

  fun addHeritage(name: String, country: String, ecosystem: String): Long {
    val dbHelper = this.writableDatabase
    val contentValues = ContentValues()
    contentValues.put("name", name)
    contentValues.put("country", country)
    contentValues.put("ecosystem", ecosystem)
    val successCode: Long = dbHelper.insert(TABLE_HERITAGES, null, contentValues)
    dbHelper.close()
    return successCode
  }

  fun getHeritages(): ArrayList<Heritage> {
    val dbHelper = this.writableDatabase
    val cursor: Cursor = dbHelper.rawQuery("SELECT * FROM $TABLE_HERITAGES", null)
    val allHeritages: ArrayList<Heritage> = ArrayList()
    if (cursor.moveToFirst()) {
      do {
        allHeritages.add(Heritage(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3)))
      } while (cursor.moveToNext())
    }
    cursor.close()
    return allHeritages
  }

  fun getHeritageWith(id: Int): Heritage? {
    val db = this.writableDatabase
    var heritage: Heritage? = null
    var cursorGames: Cursor? = null
    cursorGames = db.rawQuery("SELECT * FROM $TABLE_HERITAGES WHERE id = $id LIMIT 1", null)
    if(cursorGames.moveToFirst()){
      heritage = Heritage(cursorGames.getInt(0), cursorGames.getString(1), cursorGames.getString(2), cursorGames.getString(3))
    }
    cursorGames.close()
    return heritage
  }

  fun editHeritage(id: Int, name: String, country: String, ecosystem: String): Boolean {
    val dbHelper = this.writableDatabase
    var editionWasSuccessful: Boolean = false
    try {
      dbHelper.execSQL("UPDATE $TABLE_HERITAGES SET name = '$name', country = '$country', ecosystem = '$ecosystem' WHERE id = $id")
      editionWasSuccessful = true
    } catch (e: Exception) {
    } finally {
      dbHelper.close()
    }
    return editionWasSuccessful
  }

  fun deleteHeritage(id: Int): Boolean {
    var deletionWasSuccessful: Boolean = true
    val db = this.writableDatabase
    try{
      db.execSQL("DELETE FROM $TABLE_HERITAGES WHERE id = $id")
      deletionWasSuccessful = true
    }catch(e: Exception){
    }finally {
      db.close()
    }
    return deletionWasSuccessful
  }
}