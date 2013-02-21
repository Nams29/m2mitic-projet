package fr.istic.project.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import fr.istic.project.model.OContext;
import fr.istic.project.model.OPhoto;
import fr.istic.project.utils.FormatUtils;


public final class ApplicationDB
{
	private static ApplicationDB instance = new ApplicationDB(); // DP Singleton

	private transient ApplicationSQLiteOpenHelper dbHelper;
	private transient SQLiteDatabase database;
	
	private String[] COLS = new String[] {
			ApplicationSQLiteOpenHelper.PHOTOS_IDENTIFIER,
			ApplicationSQLiteOpenHelper.PHOTOS_DATE, 
			ApplicationSQLiteOpenHelper.PHOTOS_LOCATION, 
			ApplicationSQLiteOpenHelper.PHOTOS_NOTE, 
			ApplicationSQLiteOpenHelper.PHOTOS_DESCRIPTION,
			ApplicationSQLiteOpenHelper.PHOTOS_PATH,
			ApplicationSQLiteOpenHelper.PHOTOS_AVAILABLE,
			ApplicationSQLiteOpenHelper.PHOTOS_CONTEXT };

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
	
	public long setAllPhotosUnavailable() {
		final ContentValues contentValues = new ContentValues();
		contentValues.put(ApplicationSQLiteOpenHelper.PHOTOS_AVAILABLE, "false");
		int resultat = database.update(ApplicationSQLiteOpenHelper.PHOTOS_TABLE_NAME, contentValues, null, null);

		return resultat;
	}
	
	public long setPhotoAvailable(final String photoId) {
		final ContentValues contentValues = new ContentValues();
		contentValues.put(ApplicationSQLiteOpenHelper.PHOTOS_AVAILABLE, "true");
		int resultat = database.update(ApplicationSQLiteOpenHelper.PHOTOS_TABLE_NAME, contentValues, 
				ApplicationSQLiteOpenHelper.PHOTOS_IDENTIFIER + " = \"" + photoId+ "\"", null);

		return resultat;
	}

	public long addPhoto(final OPhoto photo)
	{
		final ContentValues contentValues = new ContentValues();
		contentValues.put(ApplicationSQLiteOpenHelper.PHOTOS_IDENTIFIER, 	photo.getIdentifier());
		contentValues.put(ApplicationSQLiteOpenHelper.PHOTOS_DATE, 			FormatUtils.dateToStringDb(photo.getDate()));
		contentValues.put(ApplicationSQLiteOpenHelper.PHOTOS_LOCATION, 		photo.getLocation());
		contentValues.put(ApplicationSQLiteOpenHelper.PHOTOS_NOTE, 			photo.getNote());
		contentValues.put(ApplicationSQLiteOpenHelper.PHOTOS_DESCRIPTION, 	photo.getDescription());
		contentValues.put(ApplicationSQLiteOpenHelper.PHOTOS_PATH, 			photo.getPath());
		contentValues.put(ApplicationSQLiteOpenHelper.PHOTOS_AVAILABLE, 	photo.getAvailable());
		if (photo.getContext() != null)
			contentValues.put(ApplicationSQLiteOpenHelper.PHOTOS_CONTEXT, 	photo.getContext().get_id());
		
		try {
			final long photoId = database.insert(ApplicationSQLiteOpenHelper.PHOTOS_TABLE_NAME, null, contentValues);
			return photoId;
		} catch (SQLiteException e) {
			return -1;
		}
	}

	public OPhoto getPhotoByIdentifier(final String photoId)
	{
		final Cursor cursor = database.query(ApplicationSQLiteOpenHelper.PHOTOS_TABLE_NAME, COLS,				
				ApplicationSQLiteOpenHelper.PHOTOS_IDENTIFIER + " = \"" + photoId+ "\"", null, null, null, null);
		cursor.moveToFirst();

		OPhoto photo = null;
		if (cursor.getCount() != 0)
			photo = buildPhotoFromCursor(cursor);

		cursor.close();
		return photo;
	}
	
	
	public OPhoto getPhotoByName(final String photoName)
	{
		final Cursor cursor = database.query(ApplicationSQLiteOpenHelper.PHOTOS_TABLE_NAME, COLS,				
				ApplicationSQLiteOpenHelper.PHOTOS_PATH + " LIKE %" + photoName, null, null, null, null);
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

		final Cursor cursor = database.query(ApplicationSQLiteOpenHelper.PHOTOS_TABLE_NAME, COLS,				
				null, null, null, null, ApplicationSQLiteOpenHelper.PHOTOS_DATE);
		cursor.moveToFirst();

		while (!cursor.isAfterLast())
		{
			photos.add(buildPhotoFromCursor(cursor));
			cursor.moveToNext();
		}

		cursor.close();
		return photos;
	}
	
	public List<OPhoto> getAllPhotosAvailable()
	{
		final List<OPhoto> photos = new ArrayList<OPhoto>();

		final Cursor cursor = database.query(ApplicationSQLiteOpenHelper.PHOTOS_TABLE_NAME, COLS,				
				ApplicationSQLiteOpenHelper.PHOTOS_AVAILABLE + " = 'true'", null, null, null, ApplicationSQLiteOpenHelper.PHOTOS_DATE);
		cursor.moveToFirst();

		while (!cursor.isAfterLast())
		{
			photos.add(buildPhotoFromCursor(cursor));
			cursor.moveToNext();
		}

		cursor.close();
		return photos;
	}
		
	public List<OPhoto> getSomePhotos(int nb)
	{
		final List<OPhoto> photos = new ArrayList<OPhoto>();

		final Cursor cursor = database.query(ApplicationSQLiteOpenHelper.PHOTOS_TABLE_NAME, COLS,				
				null, null, null, null, "RANDOM()", String.valueOf(nb));
		cursor.moveToFirst();

		while (!cursor.isAfterLast())
		{
			photos.add(buildPhotoFromCursor(cursor));
			cursor.moveToNext();
		}

		cursor.close();
		return photos;
	}
	
	/**
	 * Return the pictures taken on the given time interval
	 * @param dateStart the interval start
	 * @param dateEnd the interval date
	 * @return the photos
	 */
	public List<OPhoto> getPhotosFromDateInterval(Date dateStart, Date dateEnd) {
		List<OPhoto> photos = new ArrayList<OPhoto>();
		
		/*String query = "SELECT * " +
					   "FROM "+ApplicationSQLiteOpenHelper.PHOTOS_TABLE_NAME+" "+
					   "WHERE "+ApplicationSQLiteOpenHelper.PHOTOS_DATE+" >= datetime(\""+FormatUtils.dateToStringDb(dateStart)+"\") "+
					   "AND "+ApplicationSQLiteOpenHelper.PHOTOS_DATE+" <= datetime(\""+FormatUtils.dateToStringDb(dateEnd)+"\") "+
					   "ORDER BY "+ApplicationSQLiteOpenHelper.PHOTOS_DATE;*/
		String query = "SELECT * " +
					   "FROM "+ApplicationSQLiteOpenHelper.PHOTOS_TABLE_NAME+" "+
					   "WHERE "+ApplicationSQLiteOpenHelper.PHOTOS_DATE+" >= Datetime('2009-11-13 00:00:00')";
		database.rawQuery(query, null);
		
		return photos;
	}

	public int updatePhoto(final OPhoto photo)
	{
		final ContentValues contentValues = new ContentValues();
		contentValues.put(ApplicationSQLiteOpenHelper.PHOTOS_IDENTIFIER, 	photo.getIdentifier());
		contentValues.put(ApplicationSQLiteOpenHelper.PHOTOS_DATE, 			FormatUtils.dateToStringDb(photo.getDate()));
		contentValues.put(ApplicationSQLiteOpenHelper.PHOTOS_LOCATION, 		photo.getLocation());
		contentValues.put(ApplicationSQLiteOpenHelper.PHOTOS_NOTE, 			photo.getNote());
		contentValues.put(ApplicationSQLiteOpenHelper.PHOTOS_DESCRIPTION, 	photo.getDescription());
		contentValues.put(ApplicationSQLiteOpenHelper.PHOTOS_PATH, 			photo.getPath());
		contentValues.put(ApplicationSQLiteOpenHelper.PHOTOS_AVAILABLE, 	photo.getAvailable());
		contentValues.put(ApplicationSQLiteOpenHelper.PHOTOS_CONTEXT, 		photo.getContext().get_id());		
		int resultat = database.update(ApplicationSQLiteOpenHelper.PHOTOS_TABLE_NAME, contentValues, ApplicationSQLiteOpenHelper.PHOTOS_IDENTIFIER + "=" + photo.getIdentifier(), null);

		return resultat;
	}

	public int deletePicture(final OPhoto photo)
	{
		Log.i(getClass().getName(), "delete photo with id '" + photo.getIdentifier() + "'");
		int resultat = database.delete(ApplicationSQLiteOpenHelper.PHOTOS_TABLE_NAME, ApplicationSQLiteOpenHelper.PHOTOS_IDENTIFIER + " = " + photo.getIdentifier(), null);

		return resultat;
	}

	private OPhoto buildPhotoFromCursor(final Cursor cursor)
	{
		final OPhoto photo = new OPhoto(cursor.getString(cursor.getColumnIndex(ApplicationSQLiteOpenHelper.PHOTOS_IDENTIFIER)));
		
		photo.setDate(
				FormatUtils.dbStringToDate(cursor.getString(cursor.getColumnIndex(ApplicationSQLiteOpenHelper.PHOTOS_DATE))));
		photo.setLocation(
				cursor.getString(cursor.getColumnIndex(ApplicationSQLiteOpenHelper.PHOTOS_LOCATION)));
		photo.setNote(
				cursor.getFloat(cursor.getColumnIndex(ApplicationSQLiteOpenHelper.PHOTOS_NOTE)));
		photo.setDescription(
				cursor.getString(cursor.getColumnIndex(ApplicationSQLiteOpenHelper.PHOTOS_DESCRIPTION)));
		photo.setAvailable(
				cursor.getString(cursor.getColumnIndex(ApplicationSQLiteOpenHelper.PHOTOS_AVAILABLE)));
		photo.setPath(
				cursor.getString(cursor.getColumnIndex(ApplicationSQLiteOpenHelper.PHOTOS_PATH)));
		
		if (cursor.getLong(cursor.getColumnIndex(ApplicationSQLiteOpenHelper.PHOTOS_CONTEXT)) != 0)
			photo.setContext(
					getContext(cursor.getLong(	cursor.getColumnIndex(ApplicationSQLiteOpenHelper.PHOTOS_CONTEXT))));
		
		return photo;
	}


	/* CONTEXTS */

	public OContext addContext(final OContext context)
	{
		final ContentValues contentValues = new ContentValues();
		contentValues.put(ApplicationSQLiteOpenHelper.CONTEXTS_NAME, context.getName());
		
		try {
			final long contextId = database.insert(ApplicationSQLiteOpenHelper.CONTEXTS_TABLE_NAME, null, contentValues);
			return getContext(contextId);
		} catch (SQLiteException e) {
			return null;
		}
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

	public int updateContext(final OContext context)
	{
		final ContentValues contentValues = new ContentValues();
		contentValues.put(ApplicationSQLiteOpenHelper.CONTEXTS_NAME, context.getName());	
		int resultat = database.update(ApplicationSQLiteOpenHelper.CONTEXTS_TABLE_NAME, contentValues, ApplicationSQLiteOpenHelper.CONTEXTS_ID + "=" + context.get_id(), null);

		return resultat;
	}

	public int deleteContext(final OContext context)
	{
		Log.i(getClass().getName(), "delete context with id '" + context.get_id() + "'");
		int resultat = database.delete(ApplicationSQLiteOpenHelper.CONTEXTS_TABLE_NAME, ApplicationSQLiteOpenHelper.CONTEXTS_ID + " = " + context.get_id(), null);

		return resultat;
	}

	private OContext buildContextFromCursor(final Cursor cursor)
	{
		final OContext context = new OContext(cursor.getLong(cursor.getColumnIndex(ApplicationSQLiteOpenHelper.CONTEXTS_ID)));
		context.setName(cursor.getString(cursor.getColumnIndex(ApplicationSQLiteOpenHelper.CONTEXTS_NAME)));

		// TODO gestion du context pour chaque photo, puis ctx.addPicture(...)
		return context;
	}
}
