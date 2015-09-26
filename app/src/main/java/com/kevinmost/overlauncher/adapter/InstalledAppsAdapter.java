package com.kevinmost.overlauncher.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kevinmost.overlauncher.R;
import com.kevinmost.overlauncher.app.App;
import com.kevinmost.overlauncher.event.FilterChangedEvent;
import com.kevinmost.overlauncher.model.InstalledApp;
import com.kevinmost.overlauncher.util.PackageUtil;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class InstalledAppsAdapter extends BaseAdapter {

  @Inject
  PackageUtil packageUtil;

  @Inject
  LayoutInflater inflater;

  @Inject
  Picasso picasso;

  @Inject
  App app;

  @Inject
  Bus bus;

  private List<InstalledApp> shownAppsCache;
  private String filter;

  public InstalledAppsAdapter() {
    App.inject(this);
    bus.register(this);
    refreshShownPackagesCache();
  }

  @Subscribe
  public void onFilterTextChangedEvent(FilterChangedEvent event) {
    filter = event.newFilter;
    notifyDataSetChanged();
  }

  @Override
  public int getCount() {
    return shownAppsCache.size();
  }

  @Override
  public InstalledApp getItem(int position) {
    return shownAppsCache.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public void notifyDataSetChanged() {
    refreshShownPackagesCache();
    super.notifyDataSetChanged();
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
    shownAppsCache = packageUtil.getInstalledPackages(filter, PackageUtil.FilterMode.ONLY_START_OF_WORDS);
    Collections.sort(shownAppsCache, new Comparator<InstalledApp>() {
      @Override
      public int compare(InstalledApp lhs, InstalledApp rhs) {
        return lhs.label.toString().compareTo(rhs.label.toString());
      }
    });
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
