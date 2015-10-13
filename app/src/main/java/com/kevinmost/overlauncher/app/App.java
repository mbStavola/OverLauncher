package com.kevinmost.overlauncher.app;

import android.app.Application;
import com.kevinmost.overlauncher.dagger.AppComponent;
import com.kevinmost.overlauncher.dagger.AppModule;
import com.kevinmost.overlauncher.dagger.DaggerAppComponent;

public class App extends Application {
  private static App INSTANCE;

  private final AppModule module = new AppModule(this);

  private static void setInstance(App app) {
    if (INSTANCE != null && INSTANCE != app) {
      throw new RuntimeException("More than one instance of singleton App has been registered.");
    }
    INSTANCE = app;
  }

  public static AppComponent provideComponent() {
    return DaggerAppComponent.builder().appModule(INSTANCE.module).build();
  }

  @Override
  public void onCreate() {
    super.onCreate();
    App.setInstance(this);
  }
}
