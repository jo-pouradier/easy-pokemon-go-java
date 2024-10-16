package com.example.pokemongeo_tp;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.pokemongeo_tp.database.Initalization;
import com.example.pokemongeo_tp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // init database
        Initalization.InitPokemon(this);
        Initalization.InitObject(this);

        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.bottomNavigation.setOnItemSelectedListener(new BottomNavigationBarListener(getSupportFragmentManager()));
        binding.bottomNavigation.setSelectedItemId(R.id.pokedex);
    }

}