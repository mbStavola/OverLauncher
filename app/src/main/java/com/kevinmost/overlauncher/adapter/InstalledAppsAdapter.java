package com.kevinmost.overlauncher.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kevinmost.overlauncher.R;
import com.kevinmost.overlauncher.app.App;
import com.kevinmost.overlauncher.event.FilterChangedEvent;
import com.kevinmost.overlauncher.model.AppsCache;
import com.kevinmost.overlauncher.model.InstalledApp;
import com.kevinmost.overlauncher.util.PackageUtil;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class InstalledAppsAdapter extends BaseAdapter {

  @Inject
  LayoutInflater inflater;

  @Inject
  Picasso picasso;

  @Inject
  App app;

  @Inject
  Bus bus;

  @Inject
  AppsCache appsCache;

  private List<InstalledApp> shownAppsCache;

  public InstalledAppsAdapter() {
    App.inject(this);
    bus.register(this);
    refreshShownPackagesCache();
  }

  @Subscribe
  public void onFilterTextChangedEvent(FilterChangedEvent event) {
    shownAppsCache = filter(appsCache.getInstalledApps(), event.newFilter, PackageUtil.FilterMode.ONLY_START_OF_WORDS);
    notifyDataSetChanged();
  }

  @Override
  public int getCount() {
    if (shownAppsCache == null) {
      return appsCache.getInstalledApps().size();
    } else {
      return shownAppsCache.size();
    }
  }

  @Override
  public InstalledApp getItem(int position) {
    if (shownAppsCache == null) {
      return appsCache.getInstalledApps().get(position);
    } else {
      return shownAppsCache.get(position);
    }
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    final ViewHolder holder;
    if (convertView != null) {
      holder = ((ViewHolder) convertView.getTag());
    } else {
      convertView = inflater.inflate(R.layout.installed_app_grid, parent, false);
      holder = new ViewHolder(convertView);
      convertView.setTag(holder);
    }
    final InstalledApp thisApp = getItem(position);

    holder.icon.setImageDrawable(thisApp.icon);
    holder.label.setText(thisApp.label);

    convertView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        app.startActivity(thisApp.startAppIntent);
      }
    });

    return convertView;
  }

  private void refreshShownPackagesCache() {
    appsCache.refreshInstalledAppsCache();
  }

  private static List<InstalledApp> filter(@NonNull List<InstalledApp> unfiltered,
                                           @Nullable String filter,
                                           @Nullable PackageUtil.FilterMode filterMode) {
    filter = (filter == null ? "" : filter.trim());
    if (filter.isEmpty()) {
      return unfiltered;
    }
    if (filterMode == null) {
      filterMode = PackageUtil.FilterMode.BASIC;
    }
    filter = filter.toLowerCase();
    final List<InstalledApp> filtered = new ArrayList<>();
    for (InstalledApp anApp : unfiltered) {
      final String appLabel = anApp.label.toString().toLowerCase();
      final boolean shouldRetainApp = filterMode.shouldRetainApp(appLabel, filter);
      if (shouldRetainApp) {
        filtered.add(anApp);
      }
    }
    return filtered;
  }


  static class ViewHolder {
    @Bind(R.id.icon)
    ImageView icon;

    @Bind(R.id.label)
    TextView label;

    public ViewHolder(View root) {
      ButterKnife.bind(this, root);
    }
  }
}
