package com.example.pokemongeo_tp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.pokemongeo_tp.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    OnClickOnPokemonListener listener = new OnClickOnPokemonListener() {
        @Override
        public void onClickOnPokemon(Pokemon Pokemon) {
            showPokemonDetails(Pokemon);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.bottomNavigation.setOnItemSelectedListener( new BottomNavigationBarListener(getSupportFragmentManager()));

        showStartup();
    }
    public void showStartup() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        PokedexFragment fragment = new PokedexFragment();
        fragment.setOnClickOnPokemonListener(listener);
        transaction.replace(R.id.fragment_container,fragment);
        transaction.commit();
    }
    public void showPokemonDetails(Pokemon pokemon) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        PokemonDetailsFragment fragment = new PokemonDetailsFragment(pokemon);
        transaction.replace(R.id.fragment_container,fragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }
}