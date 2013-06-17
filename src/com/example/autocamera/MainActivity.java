package com.example.autocamera;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

public class MainActivity extends Activity {

	private static final String TAG = "AutoCamera";
    private Camera mCamera;
    private CameraPreview mPreview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       
        // Create an instance of Camera
        mCamera = getCameraInstance();
        if (mCamera==null)
        { Log.d(TAG, "Camera is NULL");}
        else
        { Log.d(TAG, "Camera Found");}
        
        
        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);
        
   	 // Add a listener to the Capture button
        Button captureButton = (Button) findViewById(R.id.button_capture);
        captureButton.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // get an image from the camera
                    mCamera.takePicture(null, null, mPicture);
                }
            }
        ); 
	    
       
    }
    

	   private PictureCallback mPicture = new PictureCallback() {

	        @Override
	        public void onPictureTaken(byte[] data, Camera camera) {

	            File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
	            if (pictureFile == null){
	                Log.d(TAG, "Error creating media file, check storage permissions: ");
	                return;
	            }

	            try {
	                FileOutputStream fos = new FileOutputStream(pictureFile);
	                fos.write(data);
	                fos.close();
	            } catch (FileNotFoundException e) {
	                Log.d(TAG, "File not found: " + e.getMessage());
	            } catch (IOException e) {
	                Log.d(TAG, "Error accessing file: " + e.getMessage());
	            }
	        }
	    };
	    

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	/** A safe way to get an instance of the Camera object. */
	public static Camera getCameraInstance(){
	    Camera c = null;
	    try {
	        c = Camera.open(); // attempt to get a Camera instance
	    }
	    catch (Exception e){
	        // Camera is not available (in use or does not exist)
	    }
	    return c; // returns null if camera is unavailable
	}
	
	
    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();              // release the camera immediately on pause event
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseCamera();              // release the camera immediately on pause event
    }
	    
    private void releaseCamera(){
        if (mCamera != null){
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }
	    
	    public static final int MEDIA_TYPE_IMAGE = 1;
	    public static final int MEDIA_TYPE_VIDEO = 2;
	    /** Create a File for saving an image or video */
	    private static File getOutputMediaFile(int type){
	        // To be safe, you should check that the SDCard is mounted
	        // using Environment.getExternalStorageState() before doing this.

	        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
	                  Environment.DIRECTORY_PICTURES), "MyCameraApp");
	        // This location works best if you want the created images to be shared
	        // between applications and persist after your app has been uninstalled.

	        // Create the storage directory if it does not exist
	        if (! mediaStorageDir.exists()){
	            if (! mediaStorageDir.mkdirs()){
	                Log.d("MyCameraApp", "failed to create directory");
	                return null;
	            }
	        }

	        // Create a media file name
	        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	        File mediaFile;
	        if (type == MEDIA_TYPE_IMAGE){
	            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
	            "IMG_"+ timeStamp + ".jpg");
	        } else if(type == MEDIA_TYPE_VIDEO) {
	            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
	            "VID_"+ timeStamp + ".mp4");
	        } else {
	            return null;
	        }

	        return mediaFile;
	    }
	    	
	    
	    

}
