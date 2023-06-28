package com.demonativemodules;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class ShakeDetectorModule extends ReactContextBaseJavaModule implements SensorEventListener {

  private final ReactApplicationContext reactContext;
  private SensorManager mSensorManager;
  private Sensor mAccelerometer;
  public ShakeDetectorModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
    mSensorManager = (SensorManager) reactContext.getSystemService(Context.SENSOR_SERVICE);
    mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    start();

  }

  @Override
  public String getName() {
    return "ShakeDetectorModule";
  }

  @ReactMethod
  public void start() {
    mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
  }

  @ReactMethod
  public void stop() {
    mSensorManager.unregisterListener(this);

  }


  @Override
  public void onSensorChanged(SensorEvent event) {
    if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER) {
      return;
    }
    float x = event.values[0];
    float y = event.values[1];
    float z = event.values[2];

    float gX = x / SensorManager.GRAVITY_EARTH;
    float gY = y / SensorManager.GRAVITY_EARTH;
    float gZ = z / SensorManager.GRAVITY_EARTH;

    // gForce will be close to 1 when there is no movement.
    float gForce = (float) Math.sqrt(gX * gX + gY * gY + gZ * gZ);

    if (gForce > 5.0) {
        reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit("RCTShowDevMenuNotification", null);

    }
  }

  @Override
  public void onAccuracyChanged(Sensor sensor, int accuracy) {
  }
}