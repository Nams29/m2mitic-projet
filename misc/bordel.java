 /*
  * BDD : 				http://stackoverflow.com/questions/9109438/how-to-use-an-existing-database-with-an-android-application/9109728#9109728
  * Image similarity : 	http://stackoverflow.com/questions/843972/image-comparison-fast-algorithm
  * External :     	    http://stackoverflow.com/questions/10282457/how-to-access-removable-storage-on-android-devices
    					http://renzhi.ca/2012/02/03/how-to-list-all-sd-cards-on-android/
    					http://stackoverflow.com/questions/7450650/how-to-list-additional-external-storage-folders-mount-points
  */


// UTILISATION D'UNE LIGNE HORIZONTALE ET D'UNE LIGNE VERTICALE EN PLEIN MILIEU (FORME UN +)  
int height = bitmap.getHeight();
int width = bitmap.getWidth();
byte[] bitmapByteArray = new byte[height+width];
System.out.println(bitmapByteArray.length);
for (int x=0; x<width; x++) { // Ligne -
	int pixel = bitmap.getPixel(x, height/2);
	bitmapByteArray[x] = (byte) pixel;
}
for (int y=0; y<height; y++) { // Ligne |
	int pixel = bitmap.getPixel(width/2, y);
	bitmapByteArray[y+width] = (byte) pixel;
}	


	 
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


// Timeline
ImageView iv = new ImageView(this);
iv.setBackgroundDrawable(getResources().getDrawable((R.drawable.polaroid_photo_frame)));
iv.setImageDrawable(Drawable.createFromPath(pic.getPath()));
iv.setLayoutParams(new LayoutParams(87, 100));
iv.setPadding(5, 5, 5, 10);
timeline.addView(iv);


// Create Hex String
StringBuffer hexString = new StringBuffer();
for (int i = 0; i < messageDigest.length; i++) {
    String h = Integer.toHexString(0xFF & messageDigest[i]);
    while (h.length() < 2)
        h = "0" + h;
    hexString.append(h);
}
this.identifier = hexString.toString();