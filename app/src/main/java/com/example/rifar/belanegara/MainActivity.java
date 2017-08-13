package com.example.rifar.belanegara;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private boolean music=false;
    String[] nama_menu = {"Modul", "Pancasila", "Undang Undang", "About"};
    int[] icon_menu = {R.drawable.module, R.drawable.pancasila, R.drawable.law, R.drawable.about};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GridView grid = (GridView) findViewById(R.id.grid_menu);
        final ImageView sound = (ImageView) findViewById(R.id.btn_sound);
        Adapter adapter = new Adapter(MainActivity.this, nama_menu, icon_menu);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (nama_menu[position]) {
                    case "Modul":
                        startActivity(new Intent(MainActivity.this, MainActivity.class));
                        break;
                    case "Pancasila":
                        startActivity(new Intent(MainActivity.this, Pancasila.class));
                        break;
                    case "Undang Undang":
                        startActivity(new Intent(MainActivity.this, MainActivity.class));
                        break;
                    case "About":
                        startActivity(new Intent(MainActivity.this, MainActivity.class));
                        break;
                }

            }
        });

        sound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!music){
                    sound.setImageResource(R.drawable.volume);
                    startService(new Intent(MainActivity.this, Sound.class));
                    music=true;
                }else{
                    sound.setImageResource(R.drawable.mute);
                    stopService(new Intent(MainActivity.this,Sound.class));
                    music=false;
                }

                }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(MainActivity.this,Sound.class));
    }


}


