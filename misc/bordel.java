 
/* MEDIASTORE */
String[] projection = { MediaStore.Images.Media._ID, MediaStore.Images.ImageColumns.DATA };
String selection = "";
String[] selectionArgs = null;
String sortOrder = null;
Cursor mImageCursor = managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, selection, selectionArgs, sortOrder);   

if (mImageCursor != null) {
    mImageCursor.moveToNext();
    String filePath = mImageCursor.getString(mImageCursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATA)); //to retrieve the filepath.
    console.setText(console.getText() + "\n" + filePath);
} else {
    Log.d("tag", "System media store is empty.");
}
    	    


/* FilenameFilter */
new FilenameFilter() {
public boolean accept(File dir, String name) {
	return name.endsWith("jpg");
}
});