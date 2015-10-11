package com.kevinmost.overlauncher.util;

import com.google.common.eventbus.Subscribe;
import com.kevinmost.overlauncher.app.App;
import com.kevinmost.overlauncher.event.AppsCacheRequestUpdateEvent;
import com.kevinmost.overlauncher.event.AppsCacheUpdatedEvent;
import com.kevinmost.overlauncher.model.InstalledApp;
import com.kevinmost.overlauncher.util.PackageUtil;
import com.squareup.otto.Bus;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AppsCache {
  @Inject
  Bus bus;

  @Inject
  PackageUtil packageUtil;

  private List<InstalledApp> installedApps;

  @Inject
  AppsCache() {
    App.inject(this);
    bus.register(this);
  }

  public List<InstalledApp> getInstalledApps() {
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
    installedApps = packageUtil.getInstalledPackages();
    Collections.sort(installedApps, new Comparator<InstalledApp>() {
      @Override
      public int compare(InstalledApp lhs, InstalledApp rhs) {
        return lhs.label.toString().compareTo(rhs.label.toString());
      }
    });
    bus.post(new AppsCacheUpdatedEvent());
  }
}
