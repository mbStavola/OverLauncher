package com.kevinmost.overlauncher.model;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;

import com.kevinmost.overlauncher.app.App;
import com.kevinmost.overlauncher.util.PackageUtil;

import javax.inject.Inject;

public class InstalledApp {

  public final Drawable icon;
  public final CharSequence label;
  public final Intent startAppIntent;
  public final ResolveInfo info;

  @Inject
  PackageManager packageManager;

  @Inject
  PackageUtil packageUtil;

  public InstalledApp(ResolveInfo info) {
    this.info = info;
    App.inject(this);
    icon = info.loadIcon(packageManager);
    label = info.loadLabel(packageManager);
    startAppIntent = packageUtil.getStartAppIntent(this);
  }

}
