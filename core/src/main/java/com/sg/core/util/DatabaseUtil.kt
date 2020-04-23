package com.sg.core.util

import android.content.Context
import android.database.Cursor
import android.util.Log
import com.sg.core.CoreApplication
import java.io.File
import java.io.FileReader
import java.io.FileWriter

class DatabaseUtil {

    private val db = CoreApplication.instance.db

    fun exportDB(context: Context, tableName: String?) {
        val exportDir = File(context.getExternalFilesDir(null), "")
        if (!exportDir.exists()) {
            exportDir.mkdirs()
        }

        val file = File(exportDir, "$tableName.csv")
        try {
            file.createNewFile()
            val csvWrite = CSVWriter(FileWriter(file))
            val curCSV: Cursor = db.query("SELECT * FROM $tableName", null)
            csvWrite.writeNext(curCSV.columnNames)
            while (curCSV.moveToNext()) {
                //Which column you want to exprort
                val arrStr = arrayOfNulls<String>(curCSV.columnCount)
                for (i in 0 until curCSV.columnCount - 1) arrStr[i] = curCSV.getString(i)
                csvWrite.writeNext(arrStr)
            }
            csvWrite.close()
            curCSV.close()
//            ToastHelper.showToast(this, "Exported", Toast.LENGTH_SHORT)
        } catch (sqlEx: Exception) {
            sqlEx.printStackTrace()
//            Log.e("MainActivity", sqlEx.message, sqlEx)
        }
    }

    fun import(context: Context, tableName: String?) {
        val csvReader = CSVReader(FileReader("${context.getExternalFilesDir(null)}/$tableName"))
        val nextLine: Array<String>?
        val count = 0
        val columns = StringBuilder();
        val value = StringBuilder();
        nextLine = csvReader.readNext()
        while (nextLine != null) {
            // nextLine[] is an array of values from the line
            nextLine.forEachIndexed { index, it ->
                if (count == 0) {
                    if (index == nextLine.size - 2) {
                        columns.append(it)
                    } else {
                        columns.append(it).append(",")
                    }
                } else {
                    if (index == nextLine.size - 2) {
                        value.append("'").append(it).append("'")
                    } else {
                        value.append("'").append(it).append("',")
                    }
                }
            }
        }
        Log.d("DatabaseUtils", "$columns ------- $value");
    }
}