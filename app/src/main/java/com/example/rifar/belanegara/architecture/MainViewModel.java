package com.example.rifar.belanegara.architecture;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.example.rifar.belanegara.R;
import com.example.rifar.belanegara.SoundController;
import com.example.rifar.belanegara.module.Behavior;
import com.example.rifar.belanegara.module.BehaviorSelector;
import com.example.rifar.belanegara.module.BindableList;
import com.example.rifar.belanegara.module.BindableMember;

/**
 * Created by asus on 8/24/2017.
 */

public class MainViewModel {
    public final BindableList<MainMenu> menus =
            BindableList.create();
    public final BindableMember<Drawable> soundIcon =
            BindableMember.create();

    private boolean mIsMuted = false;
    private Context mContext;
    private Intent mMusic;

    public MainViewModel(Context context) {
        mContext = context;
        soundIcon.set(context.getDrawable(R.drawable.volume));
    }

    public void selectMenu(int id) {
        menus.get(id).action.get().invoke(null);
    }

    public void initMusic() {
        mMusic = new Intent(mContext, SoundController.class);
        mContext.startService(mMusic);
    }

    public void stopMusic() {
        mContext.stopService(mMusic);
    }

    public void toggleMusic() {
        if(mIsMuted)
            SoundController.setVolume(0f,0f);
        else
            SoundController.setVolume(1.0f, 1.0f);
        soundIcon.set(mContext.getDrawable(mIsMuted? R.drawable.mute : R.drawable.volume));
        mIsMuted = !mIsMuted;
    }
}
