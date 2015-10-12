package com.kevinmost.overlauncher.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import com.kevinmost.overlauncher.app.App;
import com.kevinmost.overlauncher.event.AppsCacheRequestUpdateEvent;
import com.squareup.otto.Bus;

import javax.inject.Inject;

public class AppsChangedReceiver extends BroadcastReceiver {

  @Inject
  Bus bus;

  public AppsChangedReceiver() {
    super();
    App.inject(this);
    bus.register(this);
  }

  @Override
  public void onReceive(Context context, Intent intent) {
    bus.post(new AppsCacheRequestUpdateEvent());
  }
}
