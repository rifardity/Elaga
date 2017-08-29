package com.example.rifar.belanegara;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import com.example.rifar.belanegara.module.BindingUtils;

public class SoundController extends Service {
    private MediaPlayer backsound;
    private static SoundController instance;

    public IBinder onBind(Intent intent){
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        backsound = MediaPlayer.create(this, R.raw.indonesia);
        backsound.setLooping(true);
        backsound.setVolume(100,100);
        instance = this;
    }
    public int onStartCommand(Intent intent, int flags ,int startId){
        backsound.start();
        return START_STICKY;
    }

    public static void setVolume(float left, float right) {
        if(isRunning())
            instance.backsound.setVolume(left, right);
    }

    public static boolean isRunning() {
        return instance != null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        backsound.stop();
        backsound.release();
        instance = null;
    }

    public static SoundController getInstance() {
        return instance;
    }

}
