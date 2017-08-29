package com.example.rifar.belanegara.architecture;

import com.example.rifar.belanegara.module.BindableList;
import com.example.rifar.belanegara.module.BindableMember;

import java.util.Arrays;

/**
 * Created by asus on 8/29/2017.
 */

public class PasalModel {
    public final BindableMember<String> title =
            BindableMember.create();
    public final BindableMember<String> category =
            BindableMember.create();
    public final BindableList<AyatModel> ayats =
            BindableList.create();

    public PasalModel(String title, String category, AyatModel[] ayats) {
        this.title.set(title);
        this.category.set(category);
        this.ayats.addAll(Arrays.asList(ayats));
    }
}
