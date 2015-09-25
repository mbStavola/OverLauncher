package com.kevinmost.overlauncher.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.kevinmost.overlauncher.service.OverlayService;

public class MainActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    final Intent serviceIntent = new Intent(this, OverlayService.class);
    startService(serviceIntent);
    onBackPressed();
  }
}
