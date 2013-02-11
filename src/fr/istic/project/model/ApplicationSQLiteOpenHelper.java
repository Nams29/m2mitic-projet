package fr.istic.project.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ApplicationSQLiteOpenHelper extends SQLiteOpenHelper
{
	private static final String DATABASE_NAME = "application.db";
	private static final int DATABASE_VERSION = 1;
	
	/* PCONTEXTS */
	public static final String CONTEXTS_TABLE_NAME = "contexts";
	public static final String CONTEXTS_ID = "_id";
	public static final String CONTEXTS_NAME = "name";

	/* PICTURES */
	public static final String PICTURES_TABLE_NAME = "pictures";
	public static final String PICTURES_ID = "_id";
	public static final String PICTURES_IDENTIFIER = "identifier";
	public static final String PICTURES_DATE = "date";
	public static final String PICTURES_LOCATION = "location";
	public static final String PICTURES_NOTE = "note";
	public static final String PICTURES_DESCRIPTION = "description";
	public static final String PICTURES_PATH = "path";
	public static final String PICTURES_CONTEXT = "context";
	
	
	
	public ApplicationSQLiteOpenHelper(final Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(final SQLiteDatabase database)
	{
		database.execSQL("CREATE TABLE " + CONTEXTS_TABLE_NAME + " (" 
								+ CONTEXTS_ID 			+ " INTEGER PRIMARY KEY," // TODO AUTOINCREMENT ?
								+ CONTEXTS_NAME 		+ " TEXT" + ");");
		
		database.execSQL("CREATE TABLE " + PICTURES_TABLE_NAME + " (" 
								+ PICTURES_ID 			+ " INTEGER PRIMARY KEY," // TODO AUTOINCREMENT ?
								+ PICTURES_IDENTIFIER 	+ " TEXT,"
								+ PICTURES_DATE 		+ " TEXT,"
								+ PICTURES_LOCATION 	+ " TEXT," 
								+ PICTURES_NOTE 		+ " INTEGER,"
								+ PICTURES_DESCRIPTION 	+ " TEXT,"
								+ PICTURES_PATH 		+ " TEXT,"
								+ PICTURES_CONTEXT 	+ " FOREIGN KEY " + CONTEXTS_TABLE_NAME + " REFERENCES (" + CONTEXTS_ID + ") ON DELETE CASCADE" + ");");
	}

	@Override
	public void onUpgrade(final SQLiteDatabase database, final int oldVersion, final int newVersion)
	{
		Log.i(getClass().getName(), "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data !");
		
		database.execSQL("DROP TABLE IF EXISTS " + CONTEXTS_TABLE_NAME);
		database.execSQL("DROP TABLE IF EXISTS " + PICTURES_TABLE_NAME);
		onCreate(database);
	}
}