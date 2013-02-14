package fr.istic.project.model;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;


public final class ApplicationDB
{
	private static ApplicationDB instance = new ApplicationDB(); // DP Singleton
	
	private transient ApplicationSQLiteOpenHelper dbHelper;
	private transient SQLiteDatabase database;

	
	private ApplicationDB()
	{
		super();
	}

	public static ApplicationDB getInstance()
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
	
	
	/* PHOTOS */
	
	public long addPhoto(final OPhoto photo)
	{
		final ContentValues contentValues = new ContentValues();
				contentValues.put(ApplicationSQLiteOpenHelper.PHOTOS_ID, 			photo.getIdentifier());
				contentValues.put(ApplicationSQLiteOpenHelper.PHOTOS_IDENTIFIER, 	photo.getIdentifier());
				contentValues.put(ApplicationSQLiteOpenHelper.PHOTOS_DATE, 			photo.getDate());
				contentValues.put(ApplicationSQLiteOpenHelper.PHOTOS_LOCATION, 		photo.getLocation());
				contentValues.put(ApplicationSQLiteOpenHelper.PHOTOS_NOTE, 			photo.getNote());
				contentValues.put(ApplicationSQLiteOpenHelper.PHOTOS_DESCRIPTION, 	photo.getDescription());
				contentValues.put(ApplicationSQLiteOpenHelper.PHOTOS_PATH, 			photo.getPath());
				if (photo.getContext() != null)
					contentValues.put(ApplicationSQLiteOpenHelper.PHOTOS_CONTEXT, 	photo.getContext().get_id());
				photo.set_id(photo.getIdentifier());
		final long photoId = database.insert(ApplicationSQLiteOpenHelper.PHOTOS_TABLE_NAME, null, contentValues);
		
		return photoId;
	}
	
	public OPhoto getPhoto(final String photoId)
	{
		final Cursor cursor = database.query(ApplicationSQLiteOpenHelper.PHOTOS_TABLE_NAME, new String[]{
				ApplicationSQLiteOpenHelper.PHOTOS_ID,
				ApplicationSQLiteOpenHelper.PHOTOS_IDENTIFIER,
				ApplicationSQLiteOpenHelper.PHOTOS_DATE, 
				ApplicationSQLiteOpenHelper.PHOTOS_LOCATION, 
				ApplicationSQLiteOpenHelper.PHOTOS_NOTE, 
				ApplicationSQLiteOpenHelper.PHOTOS_DESCRIPTION,
				ApplicationSQLiteOpenHelper.PHOTOS_PATH,
				ApplicationSQLiteOpenHelper.PHOTOS_CONTEXT},				
				ApplicationSQLiteOpenHelper.PHOTOS_ID + " = " + photoId, null, null, null, null);
		cursor.moveToFirst();

		OPhoto photo = null;
		if (cursor.getCount() != 0)
			photo = buildPhotoFromCursor(cursor);
		
		cursor.close();
		return photo;
	}
	
	public List<OPhoto> getAllPhotos()
	{
		final List<OPhoto> photos = new ArrayList<OPhoto>();
		
		final Cursor cursor = database.query(ApplicationSQLiteOpenHelper.PHOTOS_TABLE_NAME, new String[]{
				ApplicationSQLiteOpenHelper.PHOTOS_ID,
				ApplicationSQLiteOpenHelper.PHOTOS_IDENTIFIER,
				ApplicationSQLiteOpenHelper.PHOTOS_DATE, 
				ApplicationSQLiteOpenHelper.PHOTOS_LOCATION, 
				ApplicationSQLiteOpenHelper.PHOTOS_NOTE, 
				ApplicationSQLiteOpenHelper.PHOTOS_DESCRIPTION,
				ApplicationSQLiteOpenHelper.PHOTOS_PATH,
				ApplicationSQLiteOpenHelper.PHOTOS_CONTEXT},				
				null, null, null, null, null);
		cursor.moveToFirst();

		while (!cursor.isAfterLast())
		{
			photos.add(buildPhotoFromCursor(cursor));
			cursor.moveToNext();
		}
		
		cursor.close();
		return photos;
	}
	
	public OPhoto updatePhoto(final OPhoto photo)
	{
		final ContentValues contentValues = new ContentValues();
				contentValues.put(ApplicationSQLiteOpenHelper.PHOTOS_IDENTIFIER, 	photo.getIdentifier());
				contentValues.put(ApplicationSQLiteOpenHelper.PHOTOS_DATE, 			photo.getDate());
				contentValues.put(ApplicationSQLiteOpenHelper.PHOTOS_LOCATION, 		photo.getLocation());
				contentValues.put(ApplicationSQLiteOpenHelper.PHOTOS_NOTE, 			photo.getNote());
				contentValues.put(ApplicationSQLiteOpenHelper.PHOTOS_DESCRIPTION, 	photo.getDescription());
				contentValues.put(ApplicationSQLiteOpenHelper.PHOTOS_PATH, 			photo.getPath());
				contentValues.put(ApplicationSQLiteOpenHelper.PHOTOS_CONTEXT, 		photo.getContext().get_id());		
		database.update(ApplicationSQLiteOpenHelper.PHOTOS_TABLE_NAME, contentValues, ApplicationSQLiteOpenHelper.PHOTOS_ID + "=" + photo.get_id(), null);
		
		return getPhoto(photo.get_id()); // TODO changer retour
	}
	
	public void deletePicture(final OPhoto photo)
	{
		Log.i(getClass().getName(), "delete photo with id '" + photo.get_id() + "'");
		database.delete(ApplicationSQLiteOpenHelper.PHOTOS_TABLE_NAME, ApplicationSQLiteOpenHelper.PHOTOS_ID + " = " + photo.get_id(), null);
		
		 // TODO changer retour
	}
	
	private OPhoto buildPhotoFromCursor(final Cursor cursor)
	{
		final OPhoto photo = new OPhoto(cursor.getString(cursor.getColumnIndex(ApplicationSQLiteOpenHelper.PHOTOS_ID)));
			photo.setIdentifier(			cursor.getString(	cursor.getColumnIndex(ApplicationSQLiteOpenHelper.PHOTOS_IDENTIFIER)));
			photo.setDate(					cursor.getString(	cursor.getColumnIndex(ApplicationSQLiteOpenHelper.PHOTOS_DATE)));
			photo.setLocation(				cursor.getString(	cursor.getColumnIndex(ApplicationSQLiteOpenHelper.PHOTOS_LOCATION)));
			photo.setNote(					cursor.getFloat(	cursor.getColumnIndex(ApplicationSQLiteOpenHelper.PHOTOS_NOTE)));
			photo.setDescription(			cursor.getString(	cursor.getColumnIndex(ApplicationSQLiteOpenHelper.PHOTOS_DESCRIPTION)));
			photo.setPath(					cursor.getString(	cursor.getColumnIndex(ApplicationSQLiteOpenHelper.PHOTOS_PATH)));
			if (cursor.getLong(cursor.getColumnIndex(ApplicationSQLiteOpenHelper.PHOTOS_CONTEXT)) != 0)
				photo.setContext(getContext(cursor.getLong(		cursor.getColumnIndex(ApplicationSQLiteOpenHelper.PHOTOS_CONTEXT))));
		return photo;
	}
	
	
	/* CONTEXTS */
	
	public OContext addContext(final OContext context)
	{
		final ContentValues contentValues = new ContentValues();
				contentValues.put(ApplicationSQLiteOpenHelper.CONTEXTS_NAME, context.getName());
		final long contextId = database.insert(ApplicationSQLiteOpenHelper.CONTEXTS_TABLE_NAME, null, contentValues);

		return getContext(contextId);  // TODO changer retour
	}
	
	public OContext getContext(final long contextId)
	{
		final Cursor cursor = database.query(ApplicationSQLiteOpenHelper.CONTEXTS_TABLE_NAME, new String[]{
				ApplicationSQLiteOpenHelper.CONTEXTS_ID,
				ApplicationSQLiteOpenHelper.CONTEXTS_NAME},
				ApplicationSQLiteOpenHelper.CONTEXTS_ID + " = " + contextId, null, null, null, null);
		cursor.moveToFirst();
		
		OContext context = null;
		if (cursor.getCount() != 0)
			context = buildContextFromCursor(cursor);
		
		cursor.close();
		return context;		
	}
	
	public List<OContext> getAllContexts()
	{
		final List<OContext> contexts = new ArrayList<OContext>();
		
		final Cursor cursor = database.query(ApplicationSQLiteOpenHelper.CONTEXTS_TABLE_NAME, new String[]{
				ApplicationSQLiteOpenHelper.CONTEXTS_ID,
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
	
	public OContext updateContext(final OContext context)
	{
		final ContentValues contentValues = new ContentValues();
				contentValues.put(ApplicationSQLiteOpenHelper.CONTEXTS_NAME, context.getName());	
		database.update(ApplicationSQLiteOpenHelper.CONTEXTS_TABLE_NAME, contentValues, ApplicationSQLiteOpenHelper.CONTEXTS_ID + "=" + context.get_id(), null);
		
		return getContext(context.get_id()); // TODO changer retour
	}
	
	public void deleteContext(final OContext context)
	{
		Log.i(getClass().getName(), "delete context with id '" + context.get_id() + "'");
		database.delete(ApplicationSQLiteOpenHelper.CONTEXTS_TABLE_NAME, ApplicationSQLiteOpenHelper.CONTEXTS_ID + " = " + context.get_id(), null);
		 // TODO changer retour
	}
	
	private OContext buildContextFromCursor(final Cursor cursor)
	{
		final OContext context = new OContext(cursor.getLong(cursor.getColumnIndex(ApplicationSQLiteOpenHelper.CONTEXTS_ID)));
			context.setName(cursor.getString(cursor.getColumnIndex(ApplicationSQLiteOpenHelper.CONTEXTS_NAME)));
			
			// TODO get Pictures of Context, puis ctx.addPicture(...)
		return context;
	}
}