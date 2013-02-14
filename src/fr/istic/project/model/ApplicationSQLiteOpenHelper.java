package fr.istic.project.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ApplicationSQLiteOpenHelper extends SQLiteOpenHelper
{
	public static final String 	DATABASE_NAME 		= "application.db";
	private static final int 	DATABASE_VERSION 	= 1;
	
	/* CONTEXTS */
	public static final String CONTEXTS_TABLE_NAME 	= "contexts";
	public static final String CONTEXTS_ID 			= "_id";
	public static final String CONTEXTS_NAME 		= "name";

	/* PHOTOS */
	public static final String PHOTOS_TABLE_NAME 	= "photos";
	public static final String PHOTOS_ID 			= "_id";
	public static final String PHOTOS_IDENTIFIER 	= "identifier";
	public static final String PHOTOS_DATE 			= "date";
	public static final String PHOTOS_LOCATION 		= "location";
	public static final String PHOTOS_NOTE 			= "note";
	public static final String PHOTOS_DESCRIPTION 	= "description";
	public static final String PHOTOS_PATH 			= "path";
	public static final String PHOTOS_CONTEXT 		= "context";
	
	
	
	public ApplicationSQLiteOpenHelper(final Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(final SQLiteDatabase database)
	{
		database.execSQL("CREATE TABLE " + CONTEXTS_TABLE_NAME + " (" 
								+ CONTEXTS_ID 			+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
								+ CONTEXTS_NAME 		+ " TEXT" + ");");
		
		database.execSQL("CREATE TABLE " + PHOTOS_TABLE_NAME + " (" 
								+ PHOTOS_ID 			+ " TEXT UNIQUE PRIMARY KEY,"
								+ PHOTOS_IDENTIFIER 	+ " TEXT," // TODO virer identifier partout
								+ PHOTOS_DATE 			+ " TEXT,"
								+ PHOTOS_LOCATION 		+ " TEXT," 
								+ PHOTOS_NOTE 			+ " INTEGER,"
								+ PHOTOS_DESCRIPTION 	+ " TEXT,"
								+ PHOTOS_PATH 			+ " TEXT,"
								+ PHOTOS_CONTEXT 		+ " INTEGER,"
								+ " FOREIGN KEY (" + PHOTOS_CONTEXT + ") REFERENCES " + CONTEXTS_TABLE_NAME + " (" + CONTEXTS_ID + ") ON DELETE CASCADE" + ");");
	}

	@Override
	public void onUpgrade(final SQLiteDatabase database, final int oldVersion, final int newVersion)
	{
		Log.i(getClass().getName(), "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data !");
		
		database.execSQL("DROP TABLE IF EXISTS " + CONTEXTS_TABLE_NAME);
		database.execSQL("DROP TABLE IF EXISTS " + PHOTOS_TABLE_NAME);
		onCreate(database);
	}
}