package com.example.pokemongeo_tp;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.pokemongeo_tp.database.Database;
import com.example.pokemongeo_tp.databinding.PokedexFragmentBinding;
import com.example.pokemongeo_tp.entities.OwnPokemonEntity;
import com.example.pokemongeo_tp.entities.PokemonEntity;
import com.example.pokemongeo_tp.enums.POKEMON_TYPE;
import com.example.pokemongeo_tp.threading.RequestPromise;
import com.example.pokemongeo_tp.threading.RequestThread;
import com.example.pokemongeo_tp.threading.ThreadEventListener;

import java.util.ArrayList;
import java.util.List;

public class StarterFragment extends Fragment {

    List<Pokemon> pokemonList = new ArrayList<>();
    PokemonListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        PokedexFragmentBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.starter_fragment, container, false);
        binding.pokemonList.setLayoutManager(new GridLayoutManager(
                binding.getRoot().getContext(),3));

        pokemonList = createStarterList(binding);
        adapter = new PokemonListAdapter(pokemonList);
        binding.pokemonList.setAdapter(adapter);
        SelectStarterListener listener = ListenerFactory.getOnClickOnPokemonListener(this.getParentFragmentManager());
        adapter.setOnClickOnPokemonListener(listener);
        return binding.getRoot();
    }

    public List<Pokemon> createStarterList(PokedexFragmentBinding binding) {
        // Ouverture du fichier res/raw
        List<Pokemon> pokeList = new ArrayList<>();
        RequestPromise<Context, List<PokemonEntity>> promise = new RequestPromise<>(
                new ThreadEventListener<List<PokemonEntity>>() {
                    @Override
                    public void OnEventInThread(List<PokemonEntity> data) {
                        for (PokemonEntity poke: data){
                            // map pokemon with pokemonEntity
                            pokeList.add(new Pokemon(poke));
                        }
                         // refresh view
                        new Handler(Looper.getMainLooper()).post(() -> {
                            binding.pokemonList.setAdapter(adapter);
                        });
                    }

                    @Override
                    public void OnEventInThreadReject(String error) {
                        Log.e("ERROR", "while creating pokemon from PokemonEntity. "+ error);
                    }
                },
                (Context context) -> {
                    Database db = Database.getInstance(context);
                    return db.pokemonDao().getStarterPokemon();
                },
                requireContext()
        );
        RequestThread instance = RequestThread.getInstance();
        instance.addRequest(promise);

        return pokeList;
    }

    /**
     * Affiche les details d'un Pokemon.
     * Fonction utilisé par le listener OnClickOnPokemonListener.
     *
     * @param pokemon Pokemon à afficher (donner par le listener)
     * @param manager FragmentManager de la mainActivity
     */
    public void SelectStarter(Pokemon pokemon, FragmentManager manager) {
        FragmentTransaction transaction = manager.beginTransaction();
        PokedexFragment fragment = new PokedexFragment();
        RequestPromise<Context, PokemonEntity> promise = new RequestPromise<>(
                new ThreadEventListener<PokemonEntity>() {
                    @Override
                    public void OnEventInThread(PokemonEntity data) {
                        // refresh view
                        new Handler(Looper.getMainLooper()).post(() -> {
                            transaction.replace(R.id.fragment_container, fragment);
                            transaction.addToBackStack(null);
                            transaction.commit();
                        });
                    }

                    @Override
                    public void OnEventInThreadReject(String error) {
                        Log.e("ERROR", "while creating pokemon from PokemonEntity. "+ error);
                    }
                },
                (Context context) -> {
                    Database db = Database.getInstance(context);
                    PokemonEntity pokeEntity = db.pokemonDao().getPokemonByName(pokemon.getName());
                    OwnPokemonEntity pokeOwn = new OwnPokemonEntity(pokeEntity);
                    db.ownPokemonDao().insert(pokeOwn);
                    return pokeEntity;

                },
                requireContext()
        );
        RequestThread instance = RequestThread.getInstance();
        instance.addRequest(promise);

    }

}