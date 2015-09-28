package com.kevinmost.overlauncher.model;

import com.kevinmost.overlauncher.app.App;
import com.kevinmost.overlauncher.event.AppsCacheUpdatedEvent;
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
