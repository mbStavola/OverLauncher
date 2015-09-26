package com.kevinmost.overlauncher.util;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import com.kevinmost.overlauncher.app.App;
import com.kevinmost.overlauncher.model.InstalledApp;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PackageUtil {

  @Inject
  PackageManager packageManager;

  @Inject
  PackageUtil() {
    App.inject(this);
  }

  /**
   * Given an InstalledApp object (which can be obtained from {@link this#getInstalledPackages()},
   * returns an Intent that can be fired to open that object.
   */
  public Intent getStartAppIntent(InstalledApp app) {
    final ActivityInfo activityInfo = app.info.activityInfo;
    final String packageName = activityInfo.packageName;
    final String name = activityInfo.name;
    final ComponentName componentName = new ComponentName(packageName, name);
    return new Intent(Intent.ACTION_MAIN)
        .addCategory(Intent.CATEGORY_LAUNCHER)
        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
            Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
        .setComponent(componentName);
  }

  public List<InstalledApp> getInstalledPackages() {
    final Intent launcherIntent = new Intent(Intent.ACTION_MAIN, null);
    launcherIntent.addCategory(Intent.CATEGORY_LAUNCHER);
    final List<ResolveInfo> packages = packageManager.queryIntentActivities(launcherIntent, 0);
    final List<InstalledApp> installedApps = new ArrayList<>(packages.size());
    for (ResolveInfo aPackage : packages) {
      installedApps.add(new InstalledApp(aPackage));
    }
    return installedApps;
  }
}
