package fr.theomarie.weatherapp.database

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import fr.theomarie.weatherapp.models.Ccity
import fr.theomarie.weatherapp.utils.Functions
import java.lang.Exception

class DataBase(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE_CITY = "CREATE TABLE $TABLE_NAME_CITY " +
                "($ID_CITY Integer PRIMARY KEY, $NAME_CITY TEXT, $COUNTRY_CITY TEXT, $SUBCOUNTRY_CITY TEXT, $LOVE_CITY INTEGER)"
        db?.execSQL(CREATE_TABLE_CITY)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Called when the database needs to be upgraded
    }

    fun addCity(city: Ccity): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(NAME_CITY, city.name)
        values.put(COUNTRY_CITY, city.country)
        values.put(SUBCOUNTRY_CITY, city.subcountry)
        values.put(LOVE_CITY, (if (city.love) 1 else 0))
        val _success = db.insert(TABLE_NAME_CITY, null, values)
        db.close()
        Log.v("InsertedID", "$_success")
        return (Integer.parseInt("$_success") != -1)
    }

    fun getAllCity(): ArrayList<Ccity> {
        val result: ArrayList<Ccity> = ArrayList()
        val db = readableDatabase
        val selectALLQuery = "SELECT * FROM $TABLE_NAME_CITY ORDER BY $NAME_CITY ASC"
        val cursor = db.rawQuery(selectALLQuery, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    var subcountry = ""
                    try {
                        subcountry = cursor.getString(cursor.getColumnIndex(SUBCOUNTRY_CITY))

                    } catch (e: Exception) {
                        Log.e(TAG, "getAllCity: $e", )
                    }
                    result.add(
                        Ccity(
                            cursor.getInt(cursor.getColumnIndex(ID_CITY)),
                            cursor.getString(cursor.getColumnIndex(NAME_CITY)),
                            cursor.getString(cursor.getColumnIndex(COUNTRY_CITY)),
                            subcountry,
                            null,
                            null,
                            cursor.getInt(cursor.getColumnIndex(LOVE_CITY)) == 1
                        )
                    )
                } while (cursor.moveToNext())
            }
        }
        cursor.close()
        db.close()
        return result
    }

    fun getAllLovedCity(): ArrayList<Ccity> {
        val result: ArrayList<Ccity> = ArrayList()
        val db = readableDatabase
        val selectALLQuery = "SELECT * FROM $TABLE_NAME_CITY WHERE $LOVE_CITY=1 ORDER BY $NAME_CITY ASC"
        val cursor = db.rawQuery(selectALLQuery, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    var subcountry = ""
                    try {
                        subcountry = cursor.getString(cursor.getColumnIndex(SUBCOUNTRY_CITY))

                    } catch (e: Exception) {
                        Log.e(TAG, "getAllCity: $e", )
                    }
                    result.add(
                        Ccity(
                            cursor.getInt(cursor.getColumnIndex(ID_CITY)),
                            cursor.getString(cursor.getColumnIndex(NAME_CITY)),
                            cursor.getString(cursor.getColumnIndex(COUNTRY_CITY)),
                            subcountry,
                            null,
                            null,
                            cursor.getInt(cursor.getColumnIndex(LOVE_CITY)) == 1
                        )
                    )
                } while (cursor.moveToNext())
            }
        }
        cursor.close()
        db.close()
        return result
    }

    fun getAllCity(name: String, limit: Int = 50): ArrayList<Ccity> {
        val result: ArrayList<Ccity> = ArrayList()
        val db = readableDatabase
        val selectALLQuery = "SELECT * FROM $TABLE_NAME_CITY WHERE $NAME_CITY LIKE '%$name%' ORDER BY $NAME_CITY ASC LIMIT $limit"
        val cursor = db.rawQuery(selectALLQuery, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    var subcountry = ""
                    try {
                        subcountry = cursor.getString(cursor.getColumnIndex(SUBCOUNTRY_CITY))

                    } catch (e: Exception) {
                        Log.e(TAG, "getAllCity: $e", )
                    }
                    result.add(
                        Ccity(
                            cursor.getInt(cursor.getColumnIndex(ID_CITY)),
                            cursor.getString(cursor.getColumnIndex(NAME_CITY)),
                            cursor.getString(cursor.getColumnIndex(COUNTRY_CITY)),
                            subcountry,
                            null,
                            null,
                            cursor.getInt(cursor.getColumnIndex(LOVE_CITY)) == 1
                        )
                    )
                } while (cursor.moveToNext())
            }
        }
        cursor.close()
        db.close()
        return result
    }

    fun getOneCity(name: String): Ccity? {
        val db = readableDatabase
        val selectALLQuery = "SELECT * FROM $TABLE_NAME_CITY WHERE $NAME_CITY = '%$name%' ORDER BY $NAME_CITY ASC"
        val cursor = db.rawQuery(selectALLQuery, null)
        if (cursor.count == 0) {
            return null
        }
        cursor.moveToFirst()
        var subcountry = ""
        try {
            subcountry = cursor.getString(cursor.getColumnIndex(SUBCOUNTRY_CITY))

        } catch (e: Exception) {
            Log.e("//////////", "getAllCity: $e", )
        }
        val city = Ccity(
            cursor.getInt(cursor.getColumnIndex(ID_CITY)),
            cursor.getString(cursor.getColumnIndex(NAME_CITY)),
            cursor.getString(cursor.getColumnIndex(COUNTRY_CITY)),
            subcountry,
            null,
            null,
            cursor.getInt(cursor.getColumnIndex(LOVE_CITY)) == 1
        )
        cursor.close()
        db.close()
        return city
    }

    fun updateLovedCity(city: Ccity): Boolean {
        val db = writableDatabase
        val values = ContentValues()
        values.put(LOVE_CITY, (if (city.love) 1 else 0))
        val _success = db.update(TABLE_NAME_CITY, values, "$ID_CITY=?", arrayOf(city.id.toString())) > 0
        db.close()
        if (_success) {
            Functions.logi("Success update city ::: $city")
        } else {
            Functions.loge("Error update city ::: $city")
        }
        return _success
    }

    companion object {
        private const val DB_NAME = "weather"
        private const val DB_VERSION = 1

        private const val TABLE_NAME_CITY = "city"

        private const val ID_CITY = "idC"
        private const val NAME_CITY = "name"
        private const val COUNTRY_CITY = "country"
        private const val SUBCOUNTRY_CITY = "subcountry"
        private const val LOVE_CITY = "love"
    }
}