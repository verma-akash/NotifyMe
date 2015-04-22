package com.hiveapp.notifyme;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
	static String DATABASE_NAME="userdata";
	public static final String TABLE_MON="monday";
	public static final String TABLE_TUE="tuesday";
	public static final String TABLE_WED="wednesday";
	public static final String TABLE_THU="thursday";
	public static final String TABLE_FRI="friday";
	public static final String TABLE_SAT="saturday";
	public static final String TABLE_NOTE="notestable";
	
	public static final String KEY_FNAME="classname";
	public static final String KEY_LNAME="room";
	public static final String KEY_TNAME="teachername";
	public static final String KEY_CTYPE="classtype";
	public static final String KEY_HOUR="hour";
	public static final String KEY_MIN="minute";
	
	public static final String KEY_NNAME="notename";
	
	public static final String KEY_ID="id";
	public DbHelper(Context context) {
		super(context, DATABASE_NAME, null, 1);
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_TABLE1="CREATE TABLE "+TABLE_MON+" ("+KEY_ID+" INTEGER PRIMARY KEY, "+KEY_FNAME+" TEXT, "+KEY_LNAME+" TEXT, "+KEY_TNAME+" TEXT, "+KEY_CTYPE+" INTEGER, "+KEY_HOUR+" INTEGER,"+KEY_MIN+" INTEGER)";
		db.execSQL(CREATE_TABLE1);
		String CREATE_TABLE2="CREATE TABLE "+TABLE_TUE+" ("+KEY_ID+" INTEGER PRIMARY KEY, "+KEY_FNAME+" TEXT, "+KEY_LNAME+" TEXT, "+KEY_TNAME+" TEXT, "+KEY_CTYPE+" INTEGER, "+KEY_HOUR+" INTEGER,"+KEY_MIN+" INTEGER)";
		db.execSQL(CREATE_TABLE2);
		String CREATE_TABLE3="CREATE TABLE "+TABLE_WED+" ("+KEY_ID+" INTEGER PRIMARY KEY, "+KEY_FNAME+" TEXT, "+KEY_LNAME+" TEXT, "+KEY_TNAME+" TEXT, "+KEY_CTYPE+" INTEGER, "+KEY_HOUR+" INTEGER,"+KEY_MIN+" INTEGER)";
		db.execSQL(CREATE_TABLE3);
		String CREATE_TABLE4="CREATE TABLE "+TABLE_THU+" ("+KEY_ID+" INTEGER PRIMARY KEY, "+KEY_FNAME+" TEXT, "+KEY_LNAME+" TEXT, "+KEY_TNAME+" TEXT, "+KEY_CTYPE+" INTEGER, "+KEY_HOUR+" INTEGER,"+KEY_MIN+" INTEGER)";
		db.execSQL(CREATE_TABLE4);
		String CREATE_TABLE5="CREATE TABLE "+TABLE_FRI+" ("+KEY_ID+" INTEGER PRIMARY KEY, "+KEY_FNAME+" TEXT, "+KEY_LNAME+" TEXT, "+KEY_TNAME+" TEXT, "+KEY_CTYPE+" INTEGER, "+KEY_HOUR+" INTEGER,"+KEY_MIN+" INTEGER)";
		db.execSQL(CREATE_TABLE5);
		String CREATE_TABLE6="CREATE TABLE "+TABLE_SAT+" ("+KEY_ID+" INTEGER PRIMARY KEY, "+KEY_FNAME+" TEXT, "+KEY_LNAME+" TEXT, "+KEY_TNAME+" TEXT, "+KEY_CTYPE+" INTEGER, "+KEY_HOUR+" INTEGER,"+KEY_MIN+" INTEGER)";
		db.execSQL(CREATE_TABLE6);
		String CREATE_TABLE7="CREATE TABLE "+TABLE_NOTE+" ("+KEY_ID+" INTEGER PRIMARY KEY, "+KEY_NNAME+" TEXT)";
		db.execSQL(CREATE_TABLE7);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS "+TABLE_MON);
		onCreate(db);
		db.execSQL("DROP TABLE IF EXISTS "+TABLE_TUE);
		onCreate(db);
		db.execSQL("DROP TABLE IF EXISTS "+TABLE_WED);
		onCreate(db);
		db.execSQL("DROP TABLE IF EXISTS "+TABLE_THU);
		onCreate(db);
		db.execSQL("DROP TABLE IF EXISTS "+TABLE_FRI);
		onCreate(db);
		db.execSQL("DROP TABLE IF EXISTS "+TABLE_SAT);
		onCreate(db);
		db.execSQL("DROP TABLE IF EXISTS "+TABLE_NOTE);
		onCreate(db);

	}

}
