package inf3995_01.bixi

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import inf3995_01.bixi.station.Station

private const val DATABASE_NAME = "bixi.db"
private const val DATABASE_VERSION = 1

private const val STATION_TABLE_NAME ="station"
private const val STATION_CODE ="code"
private const val STATION_KEY_ID = "id"
private const val STATION_KEY_NAME = "name"
private const val STATION_LATITUDE = "latitude"
private const val STATION_LONGITUDE = "longitude"

private const val STATION_TABLE_CREATE = """
    CREATE TABLE $STATION_TABLE_NAME (
    $STATION_KEY_ID INTEGER PRIMARY KEY,
    $STATION_CODE INTEGER,
    $STATION_KEY_NAME TEXT,
    $STATION_LATITUDE FLOAT,
    $STATION_LONGITUDE FLOAT
    )
"""

class Database(context: Context)
    :SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(STATION_TABLE_CREATE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, nemVersion: Int) {

    }

    fun createStation(station: Station):Boolean {
        val values = ContentValues()
        values.put(STATION_KEY_NAME,station.name)
        values.put(STATION_CODE, station.code)
        values.put(STATION_LATITUDE, station.latitude)
        values.put(STATION_LONGITUDE, station.longitude)
        val id = writableDatabase.insert(STATION_TABLE_NAME,null,values)
        station.id = id
        return id > 0
    }
}