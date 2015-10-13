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

@JsonSerialize(using = InstalledApp.InstalledAppSerializer.class)
@JsonDeserialize(using = InstalledApp.InstalledAppDeserializer.class)
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

  private static final String KEY_RESOLVE_INFO_FIELD = "ResolveInfo";

  static class InstalledAppSerializer extends JsonSerializer<InstalledApp> {
    @Override
    public void serialize(InstalledApp value, JsonGenerator gen, SerializerProvider serializers)
        throws IOException {
      gen.writeStartObject();
      gen.writeObjectField(KEY_RESOLVE_INFO_FIELD, value.info);
      gen.writeEndObject();
    }
  }

  static class InstalledAppDeserializer extends JsonDeserializer<InstalledApp> {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public InstalledApp deserialize(JsonParser p, DeserializationContext ctxt)
        throws IOException {
      final JsonNode rootNode = p.getCodec().readTree(p);
      final JsonNode resolveInfoNode = rootNode.get(KEY_RESOLVE_INFO_FIELD);

      final ResolveInfo resolveInfo = mapper.treeToValue(resolveInfoNode, ResolveInfo.class);

      return new InstalledApp(resolveInfo);
    }
  }
}
