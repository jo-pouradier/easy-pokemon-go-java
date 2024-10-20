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

                int type2_id = getResources().getIdentifier("feu",
                        "drawable",
                        binding.getRoot().getContext().getPackageName());
                if (pokemonEntity.type_2 != null) {
                    type2 = POKEMON_TYPE.valueOf(pokemonEntity.type_2);
                    type2_id = getResources().getIdentifier(type2.toString().toLowerCase(),
                            "drawable",
                            binding.getRoot().getContext().getPackageName());
                }
                //TO DO FINISH HERE
                int imgId = getResources().getIdentifier(pokemonEntity.image,
                        "drawable",
                        binding.getRoot().getContext().getPackageName());
                int type1ImgId = getResources().getIdentifier(type1.toString().toLowerCase(),
                        "drawable",
                        binding.getRoot().getContext().getPackageName());
                Pokemon pokemon = new Pokemon(i + 1, pokemonEntity.name, imgId, type1, type1ImgId, type2, type2_id); // Create Pokemon object
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