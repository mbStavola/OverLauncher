package com.kevinmost.overlauncher.activity;

import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;

import com.kevinmost.overlauncher.R;
import com.kevinmost.overlauncher.adapter.InstalledAppsAdapter;
import com.kevinmost.overlauncher.app.App;
import com.kevinmost.overlauncher.util.ViewUtil;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class OverlayActivity extends AppCompatActivity {

  private static final float WIDTH_PERCENTAGE = 0.9F;
  private static final float HEIGHT_PERCENTAGE = 0.7F;

  @Inject
  ViewUtil viewUtil;

  @Bind(R.id.appList)
  AbsListView appList;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    App.inject(this);
    setupWindow();
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
    initAppList();
  }

  private void initAppList() {
    final InstalledAppsAdapter appsAdapter = new InstalledAppsAdapter();
    appList.setAdapter(appsAdapter);
  }

  private void setupWindow() {
    final Window window = getWindow();
    window.setFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND,
        WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    final WindowManager.LayoutParams params = window.getAttributes();

    // Lower -> more transparent
    params.alpha = 1F;

    // Higher -> behind window is dimmer
    params.dimAmount = 0.75F;
    window.setAttributes(params);

    final Point screenDimens = viewUtil.getDisplayDimensions();
    window.setLayout((int)(screenDimens.x * WIDTH_PERCENTAGE),
        (int) (screenDimens.y * HEIGHT_PERCENTAGE));
  }
}
