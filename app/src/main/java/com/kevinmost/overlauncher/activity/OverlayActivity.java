package com.kevinmost.overlauncher.activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;
import com.kevinmost.overlauncher.R;
import com.kevinmost.overlauncher.adapter.InstalledAppsAdapter;
import com.kevinmost.overlauncher.app.App;
import com.kevinmost.overlauncher.dagger.AppComponent;
import com.kevinmost.overlauncher.event.FilterChangedEvent;
import com.squareup.otto.Bus;

public class OverlayActivity extends AppCompatActivity {

  @Bind(R.id.appList)
  AbsListView appList;

  private Bus bus;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    final AppComponent component = App.provideComponent();
    bus = component.provideBus();
    bus.register(this);
    setUpFloatingWindow();
    setContentView(R.layout.activity_overlay);
    ButterKnife.bind(this);

    initAppList();
  }

  private void initAppList() {
    final InstalledAppsAdapter appsAdapter = new InstalledAppsAdapter();
    appList.setAdapter(appsAdapter);
  }

  private void setUpFloatingWindow() {
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
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    bus.unregister(this);
    ButterKnife.unbind(this);
  }

  @OnTextChanged(R.id.filterInput)
  void onFilterTextChanged(CharSequence text) {
    bus.post(new FilterChangedEvent(text.toString()));
  }

  /**
   * If nothing consumed the touch event, we'll close this activity
   */
  @Override
  public boolean onTouchEvent(MotionEvent event) {
    finish();
    return super.onTouchEvent(event);
  }
}
