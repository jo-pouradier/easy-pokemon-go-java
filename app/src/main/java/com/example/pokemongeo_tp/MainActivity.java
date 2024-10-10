package com.example.pokemongeo_tp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.pokemongeo_tp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.bottomNavigation.setOnItemSelectedListener(new BottomNavigationBarListener(getSupportFragmentManager()));
        binding.bottomNavigation.setSelectedItemId(R.id.pokedex);
    }

}