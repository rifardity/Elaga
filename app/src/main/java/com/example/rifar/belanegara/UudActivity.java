package com.example.rifar.belanegara;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.rifar.belanegara.architecture.UudViewModel;
import com.example.rifar.belanegara.module.BindingApplicator;

public class UudActivity extends AppCompatActivity {
    private UudViewModel vm;
    private BindingApplicator applicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vm = new UudViewModel();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        applicator = new BindingApplicator();
        applicator.applyBinding(this, R.layout.uud_view, vm);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        applicator.removeBinding();
    }

}


