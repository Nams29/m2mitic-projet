package fr.istic.project.model;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

/* http://developpeur.orange.tn/developper-ensemble/tutoriels/tutoriels/android-premier-tutoriel-avec-sqlite
 * http://www.vogella.com/articles/AndroidSQLite/article.html
 * http://panierter-pinguin.de/blog/?p=138
 * http://stackoverflow.com/questions/9109438/how-to-use-an-existing-database-with-an-android-application/9109728#9109728
 * http://stackoverflow.com/questions/843972/image-comparison-fast-algorithm */
public final class ApplicationDataSource
{
	private static ApplicationDataSource instance = new ApplicationDataSource(); // DP Singleton
	
	private transient ApplicationSQLiteOpenHelper dbHelper;
	private transient SQLiteDatabase database;

	
	private ApplicationDataSource()
	{
		super();
	}

	public static ApplicationDataSource getInstance()
	{
		return instance;
	}
	
	public void initialize(final Context context)
	{
		this.dbHelper = new ApplicationSQLiteOpenHelper(context);
	}
	
	public void openDb() throws SQLiteException
	{
		database = dbHelper.getWritableDatabase();
	}
	
	public void closeDb()
	{
		dbHelper.close();
	}
	
	
	/* PICTURE */
	
	public PPicture addPicture(final PPicture pic)
	{
		final ContentValues contentValues = new ContentValues();
				contentValues.put(ApplicationSQLiteOpenHelper.PICTURES_IDENTIFIER, pic.getIdentifier());
				contentValues.put(ApplicationSQLiteOpenHelper.PICTURES_DATE, pic.getDate());
				contentValues.put(ApplicationSQLiteOpenHelper.PICTURES_LOCATION, pic.getLocation());
				contentValues.put(ApplicationSQLiteOpenHelper.PICTURES_NOTE, pic.getNote());
				contentValues.put(ApplicationSQLiteOpenHelper.PICTURES_DESCRIPTION, pic.getDescription());
				contentValues.put(ApplicationSQLiteOpenHelper.PICTURES_PATH, pic.getPath());
				contentValues.put(ApplicationSQLiteOpenHelper.PICTURES_CONTEXT, pic.getPContext().get_id());
		final long pictureId = database.insert(ApplicationSQLiteOpenHelper.PICTURES_TABLE_NAME, null, contentValues);

		return getPicture(pictureId);
	}
	
	public PPicture getPicture(final long picId)
	{
		final Cursor cursor = database.query(ApplicationSQLiteOpenHelper.PICTURES_TABLE_NAME, new String[]{
				ApplicationSQLiteOpenHelper.PICTURES_ID,
				ApplicationSQLiteOpenHelper.PICTURES_IDENTIFIER,
				ApplicationSQLiteOpenHelper.PICTURES_DATE, 
				ApplicationSQLiteOpenHelper.PICTURES_LOCATION, 
				ApplicationSQLiteOpenHelper.PICTURES_NOTE, 
				ApplicationSQLiteOpenHelper.PICTURES_DESCRIPTION,
				ApplicationSQLiteOpenHelper.PICTURES_PATH,
				ApplicationSQLiteOpenHelper.PICTURES_CONTEXT},				
				ApplicationSQLiteOpenHelper.PICTURES_ID + " = " + picId, null, null, null, null);
		cursor.moveToFirst();

		final PPicture pic = buildPictureFromCursor(cursor);
		cursor.close();
		
		return pic;
	}
	
	public List<PPicture> getAllPictures()
	{
		final List<PPicture> pictures = new ArrayList<PPicture>();
		
		final Cursor cursor = database.query(ApplicationSQLiteOpenHelper.PICTURES_TABLE_NAME, new String[]{
				ApplicationSQLiteOpenHelper.PICTURES_ID,
				ApplicationSQLiteOpenHelper.PICTURES_IDENTIFIER,
				ApplicationSQLiteOpenHelper.PICTURES_DATE, 
				ApplicationSQLiteOpenHelper.PICTURES_LOCATION, 
				ApplicationSQLiteOpenHelper.PICTURES_NOTE, 
				ApplicationSQLiteOpenHelper.PICTURES_DESCRIPTION,
				ApplicationSQLiteOpenHelper.PICTURES_PATH,
				ApplicationSQLiteOpenHelper.PICTURES_CONTEXT},				
				null, null, null, null, null);
		cursor.moveToFirst();

		while (!cursor.isAfterLast())
		{
			pictures.add(buildPictureFromCursor(cursor));
			cursor.moveToNext();
		}
		
		cursor.close();

		return pictures;
	}
	
	public PPicture updatePicture(final PPicture pic)
	{
		final ContentValues contentValues = new ContentValues();
				contentValues.put(ApplicationSQLiteOpenHelper.PICTURES_IDENTIFIER, pic.getIdentifier());
				contentValues.put(ApplicationSQLiteOpenHelper.PICTURES_DATE, pic.getDate());
				contentValues.put(ApplicationSQLiteOpenHelper.PICTURES_LOCATION, pic.getLocation());
				contentValues.put(ApplicationSQLiteOpenHelper.PICTURES_NOTE, pic.getNote());
				contentValues.put(ApplicationSQLiteOpenHelper.PICTURES_DESCRIPTION, pic.getDescription());
				contentValues.put(ApplicationSQLiteOpenHelper.PICTURES_PATH, pic.getPath());
				contentValues.put(ApplicationSQLiteOpenHelper.PICTURES_CONTEXT, pic.getPContext().get_id());		
		database.update(ApplicationSQLiteOpenHelper.PICTURES_TABLE_NAME, contentValues, ApplicationSQLiteOpenHelper.PICTURES_ID + "=" + pic.get_id(), null);
		
		return getPicture(pic.get_id());
	}
	
	public void deletePicture(final PPicture pic)
	{
		Log.i(getClass().getName(), "delete picture with id '" + pic.get_id() + "'");
		database.delete(ApplicationSQLiteOpenHelper.PICTURES_TABLE_NAME, ApplicationSQLiteOpenHelper.PICTURES_ID + " = " + pic.get_id(), null);
	}
	
	private PPicture buildPictureFromCursor(final Cursor cursor)
	{
		final PPicture pic = new PPicture(cursor.getLong(cursor.getColumnIndex(ApplicationSQLiteOpenHelper.PICTURES_ID)));
			pic.setIdentifier(cursor.getString(cursor.getColumnIndex(ApplicationSQLiteOpenHelper.PICTURES_IDENTIFIER)));
			pic.setDate(cursor.getString(cursor.getColumnIndex(ApplicationSQLiteOpenHelper.PICTURES_DATE)));
			pic.setLocation(cursor.getString(cursor.getColumnIndex(ApplicationSQLiteOpenHelper.PICTURES_LOCATION)));
			pic.setNote(cursor.getFloat(cursor.getColumnIndex(ApplicationSQLiteOpenHelper.PICTURES_NOTE)));
			pic.setDescription(cursor.getString(cursor.getColumnIndex(ApplicationSQLiteOpenHelper.PICTURES_DESCRIPTION)));
			pic.setPath(cursor.getString(cursor.getColumnIndex(ApplicationSQLiteOpenHelper.PICTURES_PATH)));
			pic.setPContext(getContext(cursor.getLong(cursor.getColumnIndex(ApplicationSQLiteOpenHelper.PICTURES_CONTEXT))));
		return pic;
	}
	
	
	/* CONTEXT */
	
	public PContext addContext(final PContext ctx)
	{
		final ContentValues contentValues = new ContentValues();
				contentValues.put(ApplicationSQLiteOpenHelper.CONTEXTS_NAME, ctx.getName());
		final long contextId = database.insert(ApplicationSQLiteOpenHelper.CONTEXTS_TABLE_NAME, null, contentValues);

		return getContext(contextId);
	}
	
	public PContext getContext(final long ctxId)
	{
		final Cursor cursor = database.query(ApplicationSQLiteOpenHelper.CONTEXTS_TABLE_NAME, new String[]{
				ApplicationSQLiteOpenHelper.CONTEXTS_NAME},
				ApplicationSQLiteOpenHelper.CONTEXTS_ID + " = " + ctxId, null, null, null, null);
		cursor.moveToFirst();

		final PContext ctx = buildContextFromCursor(cursor);
		cursor.close();
		
		return ctx;
	}
	
	public List<PContext> getAllContexts()
	{
		final List<PContext> contexts = new ArrayList<PContext>();
		
		final Cursor cursor = database.query(ApplicationSQLiteOpenHelper.CONTEXTS_TABLE_NAME, new String[]{
				ApplicationSQLiteOpenHelper.CONTEXTS_NAME},	
				null, null, null, null, null);
		cursor.moveToFirst();

		while (!cursor.isAfterLast())
		{
			contexts.add(buildContextFromCursor(cursor));
			cursor.moveToNext();
		}
		
		cursor.close();

		return contexts;
	}
	
	public PContext updateContext(final PContext ctx)
	{
		final ContentValues contentValues = new ContentValues();
				contentValues.put(ApplicationSQLiteOpenHelper.CONTEXTS_NAME, ctx.getName());	
		database.update(ApplicationSQLiteOpenHelper.CONTEXTS_TABLE_NAME, contentValues, ApplicationSQLiteOpenHelper.CONTEXTS_ID + "=" + ctx.get_id(), null);
		
		return getContext(ctx.get_id());
	}
	
	public void deleteContext(final PContext ctx)
	{
		Log.i(getClass().getName(), "delete context with id '" + ctx.get_id() + "'");
		database.delete(ApplicationSQLiteOpenHelper.CONTEXTS_TABLE_NAME, ApplicationSQLiteOpenHelper.CONTEXTS_ID + " = " + ctx.get_id(), null);
	}
	
	private PContext buildContextFromCursor(final Cursor cursor)
	{
		final PContext ctx = new PContext(cursor.getLong(cursor.getColumnIndex(ApplicationSQLiteOpenHelper.CONTEXTS_ID)));
			ctx.setName(cursor.getString(cursor.getColumnIndex(ApplicationSQLiteOpenHelper.CONTEXTS_NAME)));
			
			// TODO get Pictures of Context, puis ctx.addPicture(...)
		return ctx;
	}
}