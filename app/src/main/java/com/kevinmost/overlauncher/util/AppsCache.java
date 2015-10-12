package com.kevinmost.overlauncher.util;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.widget.Toast;
import com.kevinmost.overlauncher.BuildConfig;
import com.kevinmost.overlauncher.app.App;
import com.kevinmost.overlauncher.event.AppsCacheRequestUpdateEvent;
import com.kevinmost.overlauncher.event.AppsCacheUpdatedEvent;
import com.kevinmost.overlauncher.model.InstalledApp;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Singleton
public class AppsCache {
  @Inject
  Bus bus;

  @Inject
  PackageUtil packageUtil;

  @Inject
  App app;

  private List<InstalledApp> installedApps;

  private final SerializablePreference<InstalledApp[]> persistenceCache = new SerializablePreference<>("APPS_CACHE", InstalledApp[].class);

  @Inject
  AppsCache() {
    App.inject(this);
    bus.register(this);
    final InstalledApp[] cachedInstalledApps = persistenceCache.get();
    if (cachedInstalledApps == null) {
      this.installedApps = new ArrayList<>();
      refreshInstalledAppsCache();
    } else {
      this.installedApps = Arrays.asList(cachedInstalledApps);
    }
  }

  public @NonNull List<InstalledApp> getInstalledApps() {
    return installedApps;
  }

  /**
   * When a {@link AppsCacheRequestUpdateEvent} is fired, we will refresh our local cache, and then
   * fire a {@link AppsCacheUpdatedEvent} when it is completed.
   */
  @Subscribe
  public void onAppsCacheUpdateRequested(AppsCacheRequestUpdateEvent event) {
    refreshInstalledAppsCache();
  }

  public void refreshInstalledAppsCache() {
    if (BuildConfig.DEBUG) {
      Toast.makeText(app, "Apps updating... ", Toast.LENGTH_SHORT).show();
    }
    installedApps = packageUtil.getInstalledPackages();
    Collections.sort(installedApps, new Comparator<InstalledApp>() {
      @Override
      public int compare(InstalledApp lhs, InstalledApp rhs) {
        return lhs.label.toString().compareTo(rhs.label.toString());
      }
    });
    new AsyncTask<Void, Void, Void>() {
      @Override
      protected Void doInBackground(Void... params) {
        final InstalledApp[] installedAppsArray = new InstalledApp[installedApps.size()];
        installedApps.toArray(installedAppsArray);
        persistenceCache.set(installedAppsArray);
        return null;
      }
    }.execute();
    bus.post(new AppsCacheUpdatedEvent());
  }
}
