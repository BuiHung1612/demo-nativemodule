package com.demonativemodules;

import android.app.PictureInPictureParams;
import android.util.Rational;
import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

public class PiPModule extends ReactContextBaseJavaModule {

    public PiPModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "PictureInPictureModule";
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @ReactMethod
    public void enterPictureInPicture() {
        Activity activity = getCurrentActivity();
        if (activity == null) {
            return;
        }
        if (activity == null || Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            Log.d("[enterPictureInPicture]","Picture-in-picture mode not supported");
            return;
        }

        View view = activity.getWindow().getDecorView();
        Rational aspectRatio = new Rational(5,3);
        PictureInPictureParams.Builder builder = new PictureInPictureParams.Builder();
        builder.setAspectRatio(aspectRatio);
        activity.enterPictureInPictureMode(builder.build());
    }

}
//import android.app.Activity;
//import android.app.PictureInPictureParams;
//import android.content.res.Configuration;
//import android.os.Build;
//import android.os.Handler;
//import android.os.Looper;
//import android.util.Log;
//import android.util.Rational;
//import android.view.Gravity;
//import android.view.SurfaceView;
//import android.view.ViewGroup;
//import android.widget.FrameLayout;
//import android.widget.LinearLayout;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.RequiresApi;
//
//import com.facebook.react.bridge.Promise;
//import com.facebook.react.bridge.ReactContextBaseJavaModule;
//import com.facebook.react.bridge.ReactApplicationContext;
//import com.facebook.react.bridge.ReactMethod;
//
//@RequiresApi(api = Build.VERSION_CODES.O)
//public class PiPModule extends ReactContextBaseJavaModule {
//    private Activity currentActivity;
//    private SurfaceView videoView;
//
//    public PiPModule(ReactApplicationContext reactContext) {
//        super(reactContext);
//
//    }
//
//    @Override
//    public String getName() {
//        return "PiPModule";
//    }
//    @ReactMethod
//    public void startPiP(final Promise promise){
//        new Handler(Looper.getMainLooper()).post(new Runnable() {
//            @Override
//            public void run() {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//
//                    enterPictureInPictureMode();
//                }
//            }
//        });
//    }
//    @ReactMethod
////    public void enterPictureInPictureMode(final Promise promise) {
//    public void enterPictureInPictureMode() {
//
//        final Activity activity = getCurrentActivity();
//
//        if (activity != null && activity.isInPictureInPictureMode()) {
//            Log.d("ERR_PIP_MODE","Already in picture-in-picture mode");
////            promise.reject("ERR_PIP_MODE", "Already in picture-in-picture mode");
//            return;
//        }
//
//        if (activity == null || Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
//            Log.d("ERR_PIP_MODE","Picture-in-picture mode not supported");
//
////            promise.reject("ERR_PIP_MODE", "Picture-in-picture mode not supported");
//            return;
//        }
//
//        videoView = new SurfaceView(activity);
//
//        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
//                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
//        );
//
//        lp.gravity = Gravity.CENTER;
//        videoView.setLayoutParams(lp);
//
//        LinearLayout layout = new LinearLayout(activity);
//        layout.setOrientation(LinearLayout.VERTICAL);
//        layout.addView(videoView);
//
//        activity.setContentView(layout);
//
//        activity.enterPictureInPictureMode();
//        Log.d("ERR_PIP_MODE","Start");
//
////        promise.resolve(null);
//    }
//
//    @ReactMethod
//    public void exitPictureInPictureMode() {
//        if (currentActivity != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            currentActivity.finish();
//        }
//    }
//
//    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode) {
//        Log.i("PIPModule", "onPictureInPictureModeChanged: " + isInPictureInPictureMode);
//
//        if (isInPictureInPictureMode) {
//            videoView.setZOrderMediaOverlay(false);
//            videoView.setZOrderOnTop(true);
//        } else {
//            LinearLayout layout = new LinearLayout(currentActivity);
//            layout.addView(videoView);
//            currentActivity.setContentView(layout);
//        }
//    }

//    @Override
//    public void onHostResume() {
//        currentActivity = getCurrentActivity();
//    }
//
//    @Override
//    public void onHostPause() {
//        currentActivity = null;
//    }
//}