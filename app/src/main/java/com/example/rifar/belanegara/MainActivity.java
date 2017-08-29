package com.example.rifar.belanegara;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.example.rifar.belanegara.architecture.MainMenu;
import com.example.rifar.belanegara.architecture.MainViewModel;
import com.example.rifar.belanegara.module.Behavior;
import com.example.rifar.belanegara.module.BehaviorSelector;
import com.example.rifar.belanegara.module.BindingApplicator;

public class MainActivity extends AppCompatActivity {
    private MainViewModel vm;
    private BindingApplicator applicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vm = new MainViewModel(this.getApplicationContext());
        applicator = new BindingApplicator();
        applicator.applyBinding(this, R.layout.activity_main, vm);
        applicator.applyBehavior(MainBehavior.class, "behavior", this);

        vm.menus.add(new MainMenu(getDrawable(R.drawable.book), "Modul",
                act -> startActivity(new Intent(MainActivity.this, MainActivity.class))));
        vm.menus.add(new MainMenu(getDrawable(R.drawable.idea), "Pancasila",
                act -> startActivity(new Intent(MainActivity.this, Pancasila.class))));
        vm.menus.add(new MainMenu(getDrawable(R.drawable.laws), "Undang-Undang",
                act -> startActivity(new Intent(MainActivity.this, UudActivity.class))));
        vm.menus.add(new MainMenu(getDrawable(R.drawable.info), "About",
                act -> startActivity(new Intent(MainActivity.this, About.class))));

        vm.initMusic();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        vm.stopMusic();
        applicator.removeBinding();
    }

    private class MainBehavior extends Behavior {
        public AdapterView.OnItemClickListener onItemClick() {
            return (parent, view, position, id) -> vm.selectMenu(position);
        }

        @BehaviorSelector("sound")
        public ImageView.OnClickListener onClickSound() {
            return v -> vm.toggleMusic();
        }
    }
}


