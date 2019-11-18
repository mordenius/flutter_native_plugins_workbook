package com.example.flutter_native_plugins_workbook;

import android.os.Bundle;
import io.flutter.app.FlutterActivity;
import io.flutter.plugins.GeneratedPluginRegistrant;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;

public class MainActivity extends FlutterActivity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    GeneratedPluginRegistrant.registerWith(this);

    MethodChannel channel = new MethodChannel(getFlutterView(), "my.flutter.workbook/native-code/battery");
    channel.setMethodCallHandler(new MethodCallHandler() {
      @Override
      public void onMethodCall(MethodCall methodCall, Result result) {
        if (methodCall.method.equals("getBatteryLevel")) {
          int batteryLevel = getBatteryLevel();
          if (batteryLevel != -1) {
            result.success(batteryLevel);
          } else {
            result.error("UNAVAILABLE", "Could not get battery level.", null);
          }
        } else {
          result.notImplemented();
        }
      }
    });
  }

  private int getBatteryLevel() {
    int batteryLevel = -1;
    if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
      // BatteryManager batteryManager = (BatteryManager) getSystemManager(BATTERY_SERVICE);
      // batteryLevel = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);

      Context context = getApplicationContext();
      BatteryManager mBatteryManager = (BatteryManager) context.getSystemService(Context.BATTERY_SERVICE);
      batteryLevel = mBatteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
      
    } else {
      Intent intent = new ContextWrapper(getApplicationContext()).registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
      batteryLevel = (intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) * 100) / intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
    }

    return batteryLevel;
  }
}
