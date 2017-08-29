package com.example.rifar.belanegara.architecture;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rifar.belanegara.Pancasila;
import com.example.rifar.belanegara.R;
import com.example.rifar.belanegara.module.BindingApplicator;

/**
 * Created by asus on 8/20/2017.
 */

public class PancasilaView extends Fragment {
    private PancasilaViewModel viewModel;
    private final BindingApplicator applicator = new BindingApplicator();

    public static PancasilaView init(PancasilaViewModel vm) {
        PancasilaView fragment = new PancasilaView();
        fragment.viewModel = vm;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return applicator.applyBinding(inflater.inflate(R.layout.pancasila_view, container, false),
                R.layout.pancasila_view, viewModel);
    }
}
