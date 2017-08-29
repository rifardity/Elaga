package com.example.rifar.belanegara.architecture;

import android.graphics.drawable.Drawable;

import com.example.rifar.belanegara.module.Action;
import com.example.rifar.belanegara.module.BindableMember;

/**
 * Created by asus on 8/24/2017.
 */

public class MainMenu {
    public final BindableMember<Drawable> image =
            BindableMember.create();
    public final BindableMember<String> title =
            BindableMember.create();
    public final BindableMember<Action> action =
            BindableMember.create();

    public MainMenu(Drawable image, String title, Action action) {
        this.image.set(image);
        this.title.set(title);
        this.action.set(action);
    }
}
