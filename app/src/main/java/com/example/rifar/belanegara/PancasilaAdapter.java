package com.example.rifar.belanegara;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

import com.example.rifar.belanegara.architecture.PancasilaView;
import com.example.rifar.belanegara.architecture.PancasilaCoverView;
import com.example.rifar.belanegara.architecture.PancasilaViewModel;


public class PancasilaAdapter extends FragmentPagerAdapter {
    private PancasilaViewModel[] viewModels;

    public PancasilaAdapter(FragmentManager fm, Context context) {
        super(fm);
        viewModels = new PancasilaViewModel[] {
                new PancasilaViewModel(context.getDrawable(R.drawable.star), Color.parseColor("#27afb7"), "Ketuhanan yang Maha Esa"),
                new PancasilaViewModel(context.getDrawable(R.drawable.broken), Color.parseColor("#f55b5c"), "Kemanusiaan yang Adil dan Beradab"),
                new PancasilaViewModel(context.getDrawable(R.drawable.tree), Color.parseColor("#3498db"), "Persatuan Indonesia"),
                new PancasilaViewModel(context.getDrawable(R.drawable.bull), Color.parseColor("#3fb34f"), "Kerakyatan yang Dipimimpin oleh Khidmat Kebijaksanaan Dalam Permusyawaratan Perwakilan"),
                new PancasilaViewModel(context.getDrawable(R.drawable.wheat), Color.parseColor("#f54875"), "Keadilan Sosial bagi Seluruh Rakyat Indonesia").setBackButton(View.VISIBLE)
        };
    }

    public Fragment getItem(int posisi) {
        switch (posisi) {
            case 0:
                return new PancasilaCoverView();
            case 1:
                return PancasilaView.init(viewModels[0]);
            case 2:
                return PancasilaView.init(viewModels[1]);
            case 3:
                return PancasilaView.init(viewModels[2]);
            case 4:
                return PancasilaView.init(viewModels[3]);
            case 5:
                return PancasilaView.init(viewModels[4]);
        }
        return null;
    }

    public int getCount() {
        return 6;
    }
}
