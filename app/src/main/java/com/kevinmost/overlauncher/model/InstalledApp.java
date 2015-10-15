package com.kevinmost.overlauncher.model;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.kevinmost.overlauncher.app.App;
import com.kevinmost.overlauncher.dagger.AppComponent;
import com.kevinmost.overlauncher.util.PackageUtil;

import java.io.IOException;
import java.io.Serializable;

public class InstalledApp {

  public final ResolveInfo info;
  public final Drawable icon;
  public final CharSequence label;
  public final Intent startAppIntent;

  public InstalledApp(ResolveInfo info) {
    final AppComponent component = App.provideComponent();
    final PackageManager packageManager = component.providePackageManager();
    final PackageUtil packageUtil = component.providePackageUtil();

    this.info = info;
    icon = info.loadIcon(packageManager);
    label = info.loadLabel(packageManager);
    startAppIntent = packageUtil.getStartAppIntent(this);
  }
}
