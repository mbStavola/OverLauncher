package com.kevinmost.overlauncher.util;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.kevinmost.overlauncher.app.App;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashSet;
import java.util.Set;

@Singleton
public class Preferences {

  @Inject
  App app;

  private final SharedPreferences preferences;

  @Inject
  Preferences() {
    App.inject(this);
    preferences = PreferenceManager.getDefaultSharedPreferences(app);
    pruneAllObsoletePreferences();
  }

  private String getString(Key key) {
    return preferences.getString(key.name(), ((String) key.defaultValue));
  }

  private void setString(Key key, String value) {
    preferences.edit().putString(key.name(), value).apply();
  }

  private void pruneAllObsoletePreferences() {
    // Throw all the keys in a HashSet first for easy lookup
    final Set<String> keys = new HashSet<>();
    for (Key key : Key.values()) {
      keys.add(key.name());
    }
    final SharedPreferences.Editor pruningEditor = preferences.edit();
    for (String preferenceKey : preferences.getAll().keySet()) {
      if (!keys.contains(preferenceKey)) {
        pruningEditor.remove(preferenceKey);
      }
    }
    pruningEditor.apply();
  }

  // NOTE: Changing these enum entry names will invalidate all user settings that exist already,
  // as they will be looked up from a different key.
  public enum Key {
    APPS_CACHE((String) null)
    ;

    private final Object defaultValue;

    Key(String defaultValue) {
      this.defaultValue = defaultValue;
    }

    Key(Set<String> defaultValue) {
      this.defaultValue = defaultValue;
    }

    Key(boolean defaultValue) {
      this.defaultValue = defaultValue;
    }

    Key(int defaultValue) {
      this.defaultValue = defaultValue;
    }

    Key(long defaultValue) {
      this.defaultValue = defaultValue;
    }

    Key(float defaultValue) {
      this.defaultValue = defaultValue;
    }
  }
}
