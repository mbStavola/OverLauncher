package com.kevinmost.overlauncher.app;

import android.app.Application;

import com.kevinmost.overlauncher.module.AppModule;

import dagger.ObjectGraph;

public class App extends Application {
  private static App INSTANCE;
  private ObjectGraph objectGraph;

  public static App get() {
    return INSTANCE;
  }

  private static void setInstance(App app) {
    if (INSTANCE != null && INSTANCE != app) {
      throw new RuntimeException("More than one instance of singleton BaseApp has been registered" +
          ".");
    }
    INSTANCE = app;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    App.setInstance(this);
    resetObjectGraph();
  }

  public ObjectGraph getObjectGraph() {
    return objectGraph;
  }

  public void resetObjectGraph() {
    objectGraph = ObjectGraph.create(createDaggerModules());
    objectGraph.inject(this);
  }

  private Object[] createDaggerModules() {
    return new Object[] { new AppModule(this) };
  }
}
