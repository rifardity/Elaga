package com.example.rifar.belanegara.architecture;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.provider.Settings;
import android.view.View;

import com.example.rifar.belanegara.R;
import com.example.rifar.belanegara.module.BindableMember;
import com.example.rifar.belanegara.module.Promise;

/**
 * Created by asus on 8/18/2017.
 */

public class PancasilaViewModel {
    public BindableMember<Drawable> image =
            BindableMember.create();
    public BindableMember<Integer> color  =
            BindableMember.create();
    public BindableMember<String> text =
            BindableMember.create();
    public BindableMember<Integer> backButton =
            BindableMember.create(View.GONE);

    public PancasilaViewModel(Drawable image, int color, String text) {
        this.image.set(image);
        this.color.set(color);
        this.text.set(text);
    }

    public PancasilaViewModel setBackButton(int visibility) {
        backButton.set(visibility);
        return this;
    }
}
