package com.example.pokemongeo_tp;

import android.view.MenuItem;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavigationBarListener implements BottomNavigationView.OnNavigationItemSelectedListener {

    private final FragmentManager manager;

    BottomNavigationBarListener(FragmentManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Fragment fragment = null;

        if (item.getItemId() == R.id.pokedex) {
            PokedexFragment fragmentPokedex = new PokedexFragment();
            // TODO: set listener ? ou on peut directement le creer dans le fragment  avec le manager?
            fragmentPokedex.setOnClickOnPokemonListener(ListenerFactory.getOnClickOnPokemonListener(manager));
            fragment = fragmentPokedex;

        } else if (item.getItemId() == R.id.home) {
            fragment = new HomeFragment(); // Replace with your fragment class
        }

        if (fragment != null) {
            this.manager.beginTransaction()
                    .replace(R.id.fragment_container, fragment) // Replace with your fragment container's ID
                    .commit();
            return true;
        }

        return false;
    }

}