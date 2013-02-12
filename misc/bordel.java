 
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



/* EDITION DES DONNEES EXIF */
try {
	Log.d("tag", "file:"+f.getPath().substring(4));
	
	ExifInterface mExifTool = new ExifInterface(f.getPath().substring(4));
	Log.d("tag", "model:"+mExifTool.getAttribute(ExifInterface.TAG_MODEL));
//	mExifTool.setAttribute(ExifInterface.TAG_MODEL, "sdf2");
//	mExifTool.saveAttributes();
//	Log.d("tag", "model:"+mExifTool.getAttribute(ExifInterface.TAG_MODEL));
} catch (IOException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}