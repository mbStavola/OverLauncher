package com.kevinmost.overlauncher.util;

import android.content.SharedPreferences;
import android.util.Log;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kevinmost.overlauncher.app.App;

import javax.inject.Inject;
import java.io.IOException;

public class SerializablePreference<T> {
  private final String key;
  private final Class<T> clazz;

  @Inject
  SharedPreferences preferences;

  private final ObjectMapper mapper = new ObjectMapper();

  public SerializablePreference(String key, Class<T> clazz) {
    App.inject(this);
    this.key = key;
    this.clazz = clazz;
  }

  public void set(T value) {
    preferences.edit().putString(key, jsonifyObject(value)).apply();
  }

  public T get() {
    final String storedValue = preferences.getString(key, null);
    if (storedValue == null) {
      return null;
    }

    try {
      return mapper.readValue(storedValue, clazz);
    } catch (IOException e) {
      Log.e("OverLauncher", e.getMessage(), e);
      return null;
    }
  }

  private String jsonifyObject(Object object) {
    try {
      return mapper.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      Log.e("OverLauncher", e.getMessage(), e);
      return null;
    }
  }

}
