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
import fr.istic.project.model.OPhoto;
import fr.istic.project.utils.BitmapUtils;

public class ViewerLoader extends BaseAdapter {

    private final String TAG = "ViewerLoader";

    private Context context;

    private final List<OPhoto> photos;

    private final SparseArray<ImageView> views;

    public ViewerLoader(List<OPhoto> photos) {
        this.photos = photos;
        views = new SparseArray<ImageView>();
    }

    @Override
    public int getCount() {
        return photos.size();
    }

    @Override
    public Object getItem(int position) {
        return this.photos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return this.views.get(position).getId();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        ImageView v;

        v = views.get(position);

        if (v == null) {
            Log.d(TAG, "This view is not created. create it.");

            v = new ImageView(context);

            v.setLayoutParams(new GridView.LayoutParams(200, 200));
            v.setScaleType(ImageView.ScaleType.CENTER_CROP);

            v.setImageResource(android.R.drawable.ic_menu_gallery);

            String file = photos.get(position).getPath().toString();
            v.setTag(file);

            Bundle b = new Bundle();
            b.putString("file", file);
            b.putInt("pos", position);

            Log.d(TAG, "*before: " + b.getInt("pos") + " | " + b.getString("file"));

            new LoadImage().execute(b);

            views.put(position, v);
        }

        return v;

    }

    private class LoadImage extends AsyncTask<Bundle, Void, Bundle> {

        @Override
        protected Bundle doInBackground(Bundle... b) {

            String file = b[0].getString("file");

            Bitmap bm = fetchPhoto(file);

            Bundle bundle = new Bundle();
            bundle.putParcelable("bm", bm);
            bundle.putInt("pos", b[0].getInt("pos"));
            bundle.putString("file", file);

            return bundle;
        }

        @Override
        protected void onPostExecute(Bundle result) {
            super.onPostExecute(result);

            Log.d(TAG, "*after: " + result.getInt("pos") + " | " + result.getString("file"));

            ImageView view = views.get(result.getInt("pos"));
            view.setPadding(0, 0, 0, 0);
            view.setImageBitmap((Bitmap) result.getParcelable("bm"));
        }

    }

    private Bitmap fetchPhoto(String path) {
        Bitmap bm = null;

        Log.d(TAG, "path: " + path);

        try {
            bm = BitmapUtils.decodeSampledBitmapFromResource(path, 100, 100);

        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }

        return bm;
    }

}
