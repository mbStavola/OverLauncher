package com.kevinmost.overlauncher.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.kevinmost.overlauncher.app.App;

import javax.inject.Inject;

public class OverlayService extends Service {

  @Inject
  WindowManager windowManager;

  @Inject
  LayoutInflater layoutInflater;

  @Inject
  WindowManager.LayoutParams windowLayoutParams;

  private View overlayView;

  @Override
  public void onCreate() {
    super.onCreate();
    App.get().getObjectGraph().inject(this);
    overlayView = vendOverlayView();
    windowManager.addView(overlayView, windowLayoutParams);
  }

  private View vendOverlayView() {
    final LinearLayout overlayView = new LinearLayout(this);
    overlayView.setBackgroundColor(0x88ff0000);
    return overlayView;
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    if (overlayView == null || windowManager == null) {
      return;
    }
    windowManager.removeView(overlayView);
  }

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }
}
