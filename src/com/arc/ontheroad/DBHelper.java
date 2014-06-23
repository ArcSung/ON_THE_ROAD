package com.arc.ontheroad;

import static android.provider.BaseColumns._ID;
import static com.arc.ontheroad.DbConstants.Date;
import static com.arc.ontheroad.DbConstants.Phone;
import static com.arc.ontheroad.DbConstants.Remark;
import static com.arc.ontheroad.DbConstants.TABLE_NAME;
import static com.arc.ontheroad.DbConstants.Time;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	
	private final static String DATABASE_NAME = "demo.db";
	private final static int DATABASE_VERSION = 1;
	
	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		final String INIT_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
								  _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
								  Date + " CHAR, " +
								  Time + " CHAR, " +
								  Remark + " CHAR, " +
								  Phone + " CHAR);"; 
		db.execSQL(INIT_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
		db.execSQL(DROP_TABLE);
		onCreate(db);
	}

}
