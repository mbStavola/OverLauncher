package com.kevinmost.overlauncher.module;

import android.content.Context;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.WindowManager;

import com.kevinmost.overlauncher.BuildConfig;
import com.kevinmost.overlauncher.activity.OverlayActivity;
import com.kevinmost.overlauncher.adapter.InstalledAppsAdapter;
import com.kevinmost.overlauncher.app.App;
import com.kevinmost.overlauncher.model.InstalledApp;
import com.kevinmost.overlauncher.util.PackageUtil;
import com.kevinmost.overlauncher.util.ViewUtil;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
    injects = {
        App.class,
        InstalledApp.class,
        InstalledAppsAdapter.class,
        OverlayActivity.class,
        PackageUtil.class,
        ViewUtil.class,
    },
    library = true
)
public class AppModule {
  private final App app;

  public AppModule(App app) {
    this.app = app;
  }

  @Provides
  @Singleton
  App provideApplication() {
    return app;
  }

  @Provides
  @Singleton
  LayoutInflater provideLayoutInflater() {
    return LayoutInflater.from(app);
  }

  @Provides
  @Singleton
  PackageManager providePackageManager() {
    return app.getPackageManager();
  }

  @Provides
  @Singleton
  WindowManager provideWindowManager() {
    return getSystemService(Context.WINDOW_SERVICE);
  }

  @Provides
  @Singleton
  OkHttpClient provideOkHttpClient() {
    final OkHttpClient okHttpClient = new OkHttpClient();
    okHttpClient.setReadTimeout(10, TimeUnit.SECONDS);
    okHttpClient.setWriteTimeout(10, TimeUnit.SECONDS);
    okHttpClient.setConnectTimeout(10, TimeUnit.SECONDS);
    return okHttpClient;
  }

  @Provides
  @Singleton
  Picasso providePicasso(OkHttpClient okHttp) {
    return new Picasso.Builder(app)
        .downloader(new OkHttpDownloader(okHttp))
        .indicatorsEnabled(BuildConfig.DEBUG)
        .build();
  }

  private <T> T getSystemService(final String service) {
    //noinspection unchecked
    return (T) app.getSystemService(service);
  }
}
