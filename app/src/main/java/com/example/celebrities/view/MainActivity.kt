package com.example.celebrities.view

import android.content.Context
import android.database.sqlite.SQLiteOpenHelper
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import com.example.celebrities.R
import com.example.celebrities.database.CelebriyDatabaseHelper
import com.example.celebrities.model.Celebrity
import com.example.celebrities.util.ErrorLogger
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*
import java.lang.StringBuilder

class MainActivity : AppCompatActivity() {

    companion object {
        val fileName = "MyCelebrityList"
        val filePath = "MyFilePath"
    }

    lateinit var celebrityDBHelper: CelebriyDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        celebrityDBHelper = CelebriyDatabaseHelper(this, null)
//        getAllCelebrities()
        addCelebrityButton.setOnClickListener {
            saveToInternalStorage()
//            readFromInternalStorage()
            addNewCelebrity()

        }

        allCelebrityButton.setOnClickListener {
            readFromInternalStorage()
            getAllCelebrities()

        }

    }


    private fun saveToInternalStorage() {

        try {
            val fileWriter = openFileOutput(fileName, Context.MODE_APPEND)
            val celebrityName = celbrityNameTextview.text.toString()
            celbrityNameTextview.text.clear()
            fileWriter.write(celebrityName.toByteArray())
            fileWriter.close()
            ErrorLogger.LogError(Throwable("Success in creating file!"))

        } catch (throwable: Throwable) {
            ErrorLogger.LogError(throwable)
        }

        Environment.getExternalStorageState()
    }

    private fun readFromInternalStorage() {
        try {
            val fileInputStream: FileInputStream = openFileInput(fileName)
            val fileInputStreamReader = InputStreamReader(fileInputStream)
            val bufferedReader = BufferedReader(fileInputStreamReader)
            val stringBuilder = StringBuilder()
            var readLine: String? = null

            while ({ readLine = bufferedReader.readLine(); readLine }() != null) {
                stringBuilder.append(readLine)
            }
            celebrityDisplayTextview.text = stringBuilder.toString()

        } catch (throwable: Throwable) {
            ErrorLogger.LogError(throwable)
        }
    }

//    private fun saveToExternalStorage() {
//        try {
//            val extFilePath = File(getExternalFilesDir(filePath), fileName)
//            val fileOutputStream = FileOutputStream(extFilePath, true)
//            val readString = celbrityNameTextview.text.toString()
//            celbrityNameTextview.text.clear()
//            fileOutputStream.write(readString.toByteArray())
//            fileOutputStream.close()
//
//        } catch (throwable: Throwable) {
//            ErrorLogger.LogError(throwable)
//        }
//    }
//
//    private fun ReadFromExternalStorage() {
//        try {
//            val extFilePath = File(getExternalFilesDir(filePath), fileName)
//            val fileInputStream = FileInputStream(extFilePath)
//            val inputStreamReader = InputStreamReader(fileInputStream)
//            val bufferedReader = BufferedReader(inputStreamReader)
//            val stringBuilder = StringBuilder()
//            var readString: String? = null
//            while ({ readString = bufferedReader.readLine(); readString }() != null) (
//                    stringBuilder.append(readString)
//                    )
//            celebrityDisplayTextview = stringBuilder.toString()
//        } catch (throwable: Throwable) {
//            ErrorLogger.LogError(throwable)
//        }
//    }

    private fun addNewCelebrity() {
        val celerityName = celbrityNameTextview.text.toString().toUpperCase()
        val celebrityFavorite = 0

        celbrityNameTextview.text.clear()
        try {
            celebrityDBHelper.insertNewCelebrity(Celebrity(celerityName, celebrityFavorite))
        } catch (throwable: Throwable) {
            ErrorLogger.LogError(throwable)
        }
    }

    private fun getAllCelebrities() {
        val dbCursor = celebrityDBHelper.getAllCelebrities()
        dbCursor?.moveToFirst()
        val stringBuilder = StringBuilder()
        try {
            dbCursor?.let { myCursor ->
                while (myCursor.moveToNext()) {
                    stringBuilder.append(
                        "${myCursor.getString(myCursor.getColumnIndex(CelebriyDatabaseHelper.COLUMN_NAME))} | ${myCursor.getString(
                            myCursor.getColumnIndex(CelebriyDatabaseHelper.COLUMN_FAVORITE))
                        }\n"
                    )
                }
                myCursor.close()
            }
            celebrityDisplayTextview.text = stringBuilder.toString()
        } catch(throwable: Throwable)
        {
            ErrorLogger.LogError(throwable)
        }
    }
}
