package com.kevinmost.overlauncher.util;

import android.graphics.Point;
import android.support.annotation.NonNull;
import android.view.Display;
import android.view.WindowManager;
import com.kevinmost.overlauncher.app.App;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ViewUtil {

  @Inject
  WindowManager windowManager;

  @Inject
  ViewUtil() {
    App.inject(this);
  }

  @NonNull
  public Point getDisplayDimensions() {
    final Display defaultDisplay = windowManager.getDefaultDisplay();
    final Point size = new Point();
    defaultDisplay.getSize(size);
    return size;
  }
}
