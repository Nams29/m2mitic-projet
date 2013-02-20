package fr.istic.project.controller;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import fr.istic.project.data.ApplicationDB;
import fr.istic.project.model.OPhoto;
import fr.istic.project.utils.BitmapUtils;

public class ViewerLoader extends BaseAdapter {

	private final String TAG = "ViewerLoader"; 

	private Context context;

	// stores all data for album..
	private List<OPhoto> photos;
	
	// views stores all the loaded ImageViews linked to their
	// position in the GridView; hence <Integer, ImageView> in
	// the HashMap..
	private SparseArray<ImageView> views;

	public ViewerLoader(Context c) {
		context = c;
		// photos stores all of my data for the album, this includes
		// the filename, some other stuff I'm not using..
		
		ApplicationDB database = ApplicationDB.getInstance();
		database.openDb();
		photos = database.getSomePhotos(50);
		database.closeDb();
		
		// 'views' is a HashMap that holds all
		// of the loaded ImageViews linked to their position
		// inside the GridView. it's used for checking to see if
		// the particular ImageView has already been loaded
		// (inside the getView method) and if not, creates the
		// new ImageView and stores it in the HashMap along with
		// its position.. 
		views = new SparseArray<ImageView>();
	}

	@Override
	public int getCount() {
		return photos.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {

		ImageView v;

		// get the ImageView for this position in the GridView..
		v = views.get(position);

		// this ImageView might not be created yet..
		if (v == null) {
			Log.d(TAG, "This view is not created. create it.");

			// create a new ImageView..
			v = new ImageView(context);

			v.setLayoutParams(new GridView.LayoutParams(200, 200));
            v.setScaleType(ImageView.ScaleType.CENTER_CROP);

			// I'm setting a default image here so that you don't
			// see black nothingness.. (just using an icon that
			// comes with the Android SDK)
			v.setImageResource(android.R.drawable.ic_menu_gallery);

			// get the filename that this ImageView will hold..
			String file = photos.get(position).getPath().toString();
			v.setTag(file);

			// pass this Bundle along to the LoadImage class,
			// which is a subclass of Android's utility class
			// AsyncTask. Whatever happens in this class is
			// on its own thread.. the Bundle passes
			// the file to load and the position the photo
			// should be placed in the GridView..
			Bundle b = new Bundle ();
			b.putString("file", file);
			b.putInt("pos", position);

			// just a test to make sure that the position and
			// file name are matching before and after the
			// image has loaded..
			Log.d(TAG, "*before: " + b.getInt("pos") + " | " + b.getString("file"));

			// this executes a new thread, passing along the file
			// to load and the position via the Bundle..
			new LoadImage().execute(b);

			// puts this new ImageView and position in the HashMap.
			views.put(position, v);
		}

		// return the view to the GridView..
		// at this point, the ImageView is only displaying the
		// default icon..
		return v;

	}

	// this is the class that handles the loading of images from the cloud
	// inside another thread, separate from the main UI thread..
	private class LoadImage extends AsyncTask<Bundle, Void, Bundle> {

		@Override
		protected Bundle doInBackground(Bundle... b) {

			// get the file that was passed from the bundle..
			String file = b[0].getString("file");

			// fetchPhoto is a helper method to get the photo..
			// returns a Bitmap which we'll place inside the
			// appropriate ImageView component..
			Bitmap bm = fetchPhoto(file);

			// now that we have the bitmap (bm), we'll
			// create another bundle to pass to 'onPostExecute'.
			// this is the method that is called at the end of 
			// our task. like a callback function..
			// this time, we're not passing the filename to this
			// method, but the actual bitmap, not forgetting to
			// pass the same position along..
			Bundle bundle = new Bundle();
			bundle.putParcelable("bm", bm);
			bundle.putInt("pos", b[0].getInt("pos"));
			bundle.putString("file", file); // this is only used for testing..

			return bundle;
		}

		@Override
		protected void onPostExecute(Bundle result) {
			super.onPostExecute(result);

			// just a test to make sure that the position and
			// file name are matching before and after the
			// image has loaded..
			Log.d(TAG, "*after: " + result.getInt("pos") + " | " + result.getString("file"));

			// here's where the photo gets put into the
			// appropriate ImageView. we're retrieving the
			// ImageView from the HashMap according to
			// the position..
			ImageView view = views.get(result.getInt("pos"));

			// then we set the bitmap into that view. and that's it.
			view.setImageBitmap((Bitmap) result.getParcelable("bm"));
		}

	}

	// this is a helper method to retrieve the photo from the cloud..
	private Bitmap fetchPhoto (String path) {
		Bitmap bm = null;
		
		Log.d(TAG, "path: " + path);

		try {
			// wrapping the imageContentInputStream with FlushedInputStream.
			bm = BitmapUtils.decodeSampledBitmapFromResource(path, 100, 100);

		}
		catch (Exception e) {
			Log.e(TAG, e.getMessage(), e);
		}

		return bm;
	}

}
