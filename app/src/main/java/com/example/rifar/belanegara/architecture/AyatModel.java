package com.example.rifar.belanegara.architecture;

import com.example.rifar.belanegara.module.BindableMember;

/**
 * Created by asus on 8/29/2017.
 */

public class AyatModel {
    public final BindableMember<String> title =
            BindableMember.create();
    public final BindableMember<String> text =
            BindableMember.create();

    public AyatModel(String title, String text) {
        this.text.set(text);
        this.title.set(title);
    }
}
