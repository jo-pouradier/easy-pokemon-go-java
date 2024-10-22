package com.example.pokemongeo_tp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.pokemongeo_tp.database.Database;
import com.example.pokemongeo_tp.databinding.PokedexFragmentBinding;
import com.example.pokemongeo_tp.entities.PokemonEntity;
import com.example.pokemongeo_tp.enums.POKEMON_TYPE;

import java.util.ArrayList;
import java.util.List;

public class PokedexFragment extends Fragment {

    List<Pokemon> pokemonList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        PokedexFragmentBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.pokedex_fragment, container, false);
        binding.pokemonList.setLayoutManager(new LinearLayoutManager(
                binding.getRoot().getContext()));

        pokemonList = createPokemonList(binding);
        PokemonListAdapter adapter = new PokemonListAdapter(pokemonList);
        binding.pokemonList.setAdapter(adapter);
        OnClickOnPokemonListener listener = ListenerFactory.getOnClickOnPokemonListener(this.getParentFragmentManager());
        adapter.setOnClickOnPokemonListener(listener);
        return binding.getRoot();
    }

    public List<Pokemon> createPokemonList(PokedexFragmentBinding binding) {
        // Ouverture du fichier res/raw
        List<Pokemon> pokeList = new ArrayList<>();

        List<PokemonEntity> pokemonEntities = Database.createPokemonListFromJson(binding.getRoot().getContext().getResources());

        try {
            for (int i = 0; i < pokemonEntities.size(); i++) {
                PokemonEntity pokemonEntity = pokemonEntities.get(i);
                POKEMON_TYPE type1 = POKEMON_TYPE.valueOf(pokemonEntity.type_1);
                POKEMON_TYPE type2 = null;

                int iconType1ID, iconType2ID, iconPokemonID;
                try{
                    iconType1ID = R.drawable.class.getDeclaredField(pokemonEntity.type_1.toLowerCase()).getInt(null);
                } catch (Exception e) {
                    iconType1ID = R.drawable.feu;
                }
                try{
                    if (pokemonEntity.type_2 != null) {
                        type2 = POKEMON_TYPE.valueOf(pokemonEntity.type_2);
                        iconType2ID = R.drawable.class.getDeclaredField(pokemonEntity.type_2.toLowerCase()).getInt(null);
                    } else {
                        iconType2ID = R.drawable.normal;
                    }
                } catch (Exception e) {
                    iconType2ID = R.drawable.normal;
                }
                try {
                    iconPokemonID = R.drawable.class.getDeclaredField(pokemonEntity.image).getInt(null);
                } catch (Exception e) {
                    iconPokemonID = R.drawable.normal;
                }

                Pokemon pokemon = new Pokemon(i + 1, pokemonEntity.name, iconPokemonID, type1, iconType1ID, type2, iconType2ID); // Create Pokemon object
                pokeList.add(pokemon);
            }
        } catch (Exception e) {
            Log.e("ERROR", "createPokemonList", e);
        }
        return pokeList;
    }

    /**
     * Affiche les details d'un Pokemon.
     * Fonction utilisé par le listener OnClickOnPokemonListener.
     *
     * @param pokemon Pokemon à afficher (donner par le listener)
     * @param manager FragmentManager de la mainActivity
     */
    static public void showPokemonDetails(Pokemon pokemon, FragmentManager manager) {
        FragmentTransaction transaction = manager.beginTransaction();
        PokemonDetailsFragment fragment = new PokemonDetailsFragment(pokemon);
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}