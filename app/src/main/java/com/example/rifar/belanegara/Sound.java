package com.example.rifar.belanegara;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class Sound extends Service {
    MediaPlayer backsound;

    public IBinder onBind(Intent arg0){
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        backsound = MediaPlayer.create(this, R.raw.indonesia);
        backsound.setLooping(true);
        backsound.setVolume(100,100);
    }
    public  int onStartCommand(Intent intent, int flags ,int startId){
        backsound.start();
        return START_STICKY;
    }

    public void onPause(){
        backsound.pause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        backsound.stop();
        backsound.release();
    }

}
