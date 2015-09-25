package com.kevinmost.overlauncher.module;

import android.accounts.AccountManager;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.DownloadManager;
import android.app.NotificationManager;
import android.app.SearchManager;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.hardware.SensorManager;
import android.hardware.input.InputManager;
import android.hardware.usb.UsbManager;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.nfc.NfcManager;
import android.os.PowerManager;
import android.os.Vibrator;
import android.os.storage.StorageManager;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.kevinmost.overlauncher.app.App;
import com.kevinmost.overlauncher.service.OverlayService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
    injects = {
        App.class,
        OverlayService.class,
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
  public WindowManager.LayoutParams provideWindowLayoutParams() {
    return new WindowManager.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT,
        WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
        PixelFormat.TRANSLUCENT
    );
  }

  @Provides
  @Singleton
  public App provideApplication() {
    return app;
  }

  @Provides
  @Singleton
  public Context provideContext() {
    return app;
  }

  @Provides
  @Singleton
  public Resources provideResources() {
    return app.getResources();
  }

  @Provides
  @Singleton
  public LayoutInflater provideLayoutInflater() {
    return getSystemService(Context.LAYOUT_INFLATER_SERVICE);
  }

  @Provides
  @Singleton
  public AssetManager provideAssetManager() {
    return app.getAssets();
  }

  @Provides
  @Singleton
  public AccountManager provideAccountManager() {
    return getSystemService(Context.ACCOUNT_SERVICE);
  }

  @Provides
  @Singleton
  public ActivityManager provideActivityManager() {
    return getSystemService(Context.ACTIVITY_SERVICE);
  }

  @Provides
  @Singleton
  public AlarmManager provideAlarmManager() {
    return getSystemService(Context.ALARM_SERVICE);
  }

  @Provides
  @Singleton
  public AudioManager provideAudioManager() {
    return getSystemService(Context.AUDIO_SERVICE);
  }

  @Provides
  @Singleton
  public ClipboardManager provideClipboardManager() {
    return getSystemService(Context.CLIPBOARD_SERVICE);
  }

  @Provides
  @Singleton
  public ConnectivityManager provideConnectivityManager() {
    return getSystemService(Context.CONNECTIVITY_SERVICE);
  }

  @Provides
  @Singleton
  public DownloadManager provideDownloadManager() {
    return getSystemService(Context.DOWNLOAD_SERVICE);
  }

  @Provides
  @Singleton
  public InputManager provideInputManager() {
    return getSystemService(Context.INPUT_SERVICE);
  }

  @Provides
  @Singleton
  public LocationManager provideLocationManager() {
    return getSystemService(Context.LOCATION_SERVICE);
  }

  @Provides
  @Singleton
  public NfcManager provideNfcManager() {
    return getSystemService(Context.NFC_SERVICE);
  }

  @Provides
  @Singleton
  public NotificationManager provideNotificationManager() {
    return getSystemService(Context.NOTIFICATION_SERVICE);
  }

  @Provides
  @Singleton
  public PowerManager providePowerManager() {
    return getSystemService(Context.POWER_SERVICE);
  }

  @Provides
  @Singleton
  public SearchManager provideSearchManager() {
    return getSystemService(Context.SEARCH_SERVICE);
  }

  @Provides
  @Singleton
  public SensorManager provideSensorManager() {
    return getSystemService(Context.SENSOR_SERVICE);
  }

  @Provides
  @Singleton
  public StorageManager provideStorageManager() {
    return getSystemService(Context.STORAGE_SERVICE);
  }

  @Provides
  @Singleton
  public TelephonyManager provideTelephonyManager() {
    return getSystemService(Context.TELEPHONY_SERVICE);
  }

  @Provides
  @Singleton
  public UsbManager provideUsbManager() {
    return getSystemService(Context.USB_SERVICE);
  }

  @Provides
  @Singleton
  public Vibrator provideVibrator() {
    return getSystemService(Context.VIBRATOR_SERVICE);
  }

  @Provides
  @Singleton
  public WifiManager provideWifiManager() {
    return getSystemService(Context.WIFI_SERVICE);
  }

  @Provides
  @Singleton
  public WindowManager provideWindowManager() {
    return getSystemService(Context.WINDOW_SERVICE);
  }

  private <T> T getSystemService(final String service) {
    //noinspection unchecked
    return (T) app.getSystemService(service);
  }
}
