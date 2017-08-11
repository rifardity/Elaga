package com.example.rifar.belanegara;

import android.content.Context;
import android.media.MediaPlayer;

public class Sound {
    public static MediaPlayer backsound;

    public Sound(Context cntx) {
        backsound = MediaPlayer.create(cntx, R.raw.indonesia);
        backsound.setLooping(true);
        backsound.start();

    }
}
