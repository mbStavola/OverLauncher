package com.kevinmost.overlauncher.app;

import android.app.Application;

import com.kevinmost.overlauncher.module.AppModule;

import dagger.ObjectGraph;

public class App extends Application {
  /**
   * This instance should never be directly exposed! Objects that want access to the app instance
   * should inject themselves (see: {@link this#inject(Object)} and then inject the app instance.
   */
  private static App INSTANCE;

  private ObjectGraph objectGraph;

  private static void setInstance(App app) {
    if (INSTANCE != null && INSTANCE != app) {
      throw new RuntimeException("More than one instance of singleton App has been registered.");
    }
    INSTANCE = app;
  }

  /**
   * @param objectToInject The object that wants to participate in the app-wide object graph.
   */
  public static void inject(Object objectToInject) {
    INSTANCE.objectGraph.inject(objectToInject);
  }

  /**
   * Can be used when creating an object graph that is an extension of this base object graph.
   * For example, activities can create object graphs that exist only within their lifecycles that
   * contain extra providers, by passing those modules into this method, and then self-injecting
   * with the returned object graph.
   */
  public ObjectGraph createExtendedObjectGraph(Object... extraModules) {
    return objectGraph.plus(extraModules);
  }

  @Override
  public void onCreate() {
    super.onCreate();
    App.setInstance(this);
    resetObjectGraph();
  }

  public void resetObjectGraph() {
    objectGraph = ObjectGraph.create(createDaggerModules());
    objectGraph.inject(this);
  }

  private Object[] createDaggerModules() {
    return new Object[]{new AppModule(this)};
  }
}
