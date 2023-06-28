package com.demonativemodules;
import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.BaseActivityEventListener;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.HashMap;
import java.util.UUID;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MyModule extends ReactContextBaseJavaModule {
    private static final String DURATION_SHORT_KEY = "SHORT";
    private static final String DURATION_LONG_KEY = "LONG";

    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int IMAGE_PICKER_REQUEST = 467081;
    private static final int CAMERA_CODE = 161200;
    private static final String E_CAMERA_CANCELLED = "E_CAMERA_CANCELLED";
    private static final String E_FAILED_TO_SHOW_CAMERA = "E_CAMERA_CANCELLED";
    private static final String E_ACTIVITY_DOES_NOT_EXIST = "E_ACTIVITY_DOES_NOT_EXIST";
    private static final String E_PICKER_CANCELLED = "E_PICKER_CANCELLED";
    private static final String E_FAILED_TO_SHOW_PICKER = "E_FAILED_TO_SHOW_PICKER";
    private static final String E_NO_IMAGE_DATA_FOUND = "E_NO_IMAGE_DATA_FOUND";

    private Promise mPickerPromise;
    private Promise mCameraPromise;

    @Override
    public String getName() {
        return "CameraModule";
    }
    private final ActivityEventListener mActivityEventListener = new BaseActivityEventListener() {

        @Override
        public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent intent) {
            if (requestCode == IMAGE_PICKER_REQUEST) {
                if (mPickerPromise != null) {
                    if (resultCode == Activity.RESULT_CANCELED) {
                        mPickerPromise.reject(E_PICKER_CANCELLED, "Image picker was cancelled");
                    } else if (resultCode == Activity.RESULT_OK) {
                        Uri uri = intent.getData();

                        if (uri == null) {
                            mPickerPromise.reject(E_NO_IMAGE_DATA_FOUND, "No image data found");
                        } else {
                            mPickerPromise.resolve(uri.toString());
                        }
                    }

                    mPickerPromise = null;
                }
            }
            else if(requestCode == CAMERA_CODE){
                if(mCameraPromise!=null){
                    if (resultCode == Activity.RESULT_CANCELED) {
                        mCameraPromise.reject(E_CAMERA_CANCELLED, "Camera was cancelled");
                    }
                    else if(resultCode == Activity.RESULT_OK){
                        Bitmap image = (Bitmap) intent.getExtras().get("data");
                        //create a file to write bitmap data
                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        image.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                        String file = MediaStore.Images.Media.insertImage(context.getContentResolver(), image, "Title", null);
                        mCameraPromise.resolve(file);
                    }
                    mCameraPromise = null;
                }
            }
        }
    };
    ReactApplicationContext context = getReactApplicationContext();
    MyModule(ReactApplicationContext reactContext) {
        super(reactContext);
        // Add the listener for `onActivityResult`
        reactContext.addActivityEventListener(mActivityEventListener);

    }

    @ReactMethod
    public void pickImage(final Promise promise) {
        Activity currentActivity = getCurrentActivity();

        if (currentActivity == null) {
            promise.reject(E_ACTIVITY_DOES_NOT_EXIST, "Activity doesn't exist");
            return;
        }

        // Store the promise to resolve/reject when picker returns data
        mPickerPromise = promise;

        try {
            final Intent galleryIntent = new Intent(Intent.ACTION_PICK);

            galleryIntent.setType("image/*");

            final Intent chooserIntent = Intent.createChooser(galleryIntent, "Pick an image");

            currentActivity.startActivityForResult(chooserIntent, IMAGE_PICKER_REQUEST);
        } catch (Exception e) {
            mPickerPromise.reject(E_FAILED_TO_SHOW_PICKER, e);
            mPickerPromise = null;
        }
    }



    @Override
    public Map<String, Object> getConstants() {
        final Map<String, Object> constants = new HashMap<>();
        constants.put(DURATION_SHORT_KEY, Toast.LENGTH_SHORT);
        constants.put(DURATION_LONG_KEY, Toast.LENGTH_LONG);
        return constants;
    }



    @ReactMethod
    public void createCalendarEvent(String name, String location, Callback callback) {
        Log.d("CalendarModule", "Create event called with name: " + name
                + " and location: " + location);
        callback.invoke("data returned from calendarModule");
    }

    @ReactMethod
    public void showToast(String message, int duration) {
        Toast.makeText(getReactApplicationContext(), message, duration).show();
    }

    @ReactMethod
    public void openCamera(final Promise promise)
    {
        // Checking if permission is not granted
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(context.getCurrentActivity(), new String[] { Manifest.permission.CAMERA }, CAMERA_PERMISSION_CODE);
        }
        else {
            try {

                Activity currentActivity = getCurrentActivity();
                if (currentActivity == null) {
                    promise.reject(E_ACTIVITY_DOES_NOT_EXIST, "Activity doesn't exist");
                    return;
                }
                mCameraPromise = promise;

                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                currentActivity.startActivityForResult(cameraIntent, CAMERA_CODE);
            }
            catch (Exception e){
                Log.d("[openCamera]",""+e);
                if(mCameraPromise != null){
                    mCameraPromise.reject(E_FAILED_TO_SHOW_CAMERA, e);
                }

                mCameraPromise = null;
            }
        }
    }


}


