package com.example.rifar.belanegara;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


public class PancasilaAdapter extends FragmentPagerAdapter {
    public PancasilaAdapter(FragmentManager fm) {
        super(fm);
    }

    public Fragment getItem(int posisi) {
        switch (posisi) {
            case 0:
                return new Pancasila1();
            case 1:
                return new Pancasila2();
            case 2:
                return new Pancasila3();
            case 3:
                return new Pancasila4();
            case 4:
                return new Pancasila5();
            case 5:
                return new Pancasila6();
        }
        return null;
    }

    public int getCount() {
        return 6;
    }
}
