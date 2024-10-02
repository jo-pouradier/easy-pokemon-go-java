package com.example.pokemongeo_tp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.pokemongeo_tp.databinding.PokedexFragmentBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class PokedexFragment extends Fragment {

    List<Pokemon> pokemonList = new ArrayList<>();

    private OnClickOnPokemonListener listener;
    public void setOnClickOnPokemonListener(OnClickOnPokemonListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        PokedexFragmentBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.pokedex_fragment, container, false);
        binding.pokemonList.setLayoutManager(new LinearLayoutManager(
                binding.getRoot().getContext()));


        //call createpokemonlist
        createPokemonList(binding);
        PokemonListAdapter adapter = new PokemonListAdapter(pokemonList);
        binding.pokemonList.setAdapter(adapter);
        adapter.setOnClickOnPokemonListener(listener);
        return binding.getRoot();
    }

    public void createPokemonList(PokedexFragmentBinding binding) {
        // Ouverture du fichier res/raw
        InputStreamReader isr = new InputStreamReader(getResources().openRawResource(R.raw.poke));

        // Ouverture du fichier dans assets
        // InputStreamReader
        // isr =
        // new InputStreamReader(getResources().getAssets().open("poke.json"));
        BufferedReader reader = new BufferedReader(isr);
        StringBuilder builder = new StringBuilder();
        String data = "";
        //lecture du fichier.data == null => EOF
        while (data != null) {
            try {
                data = reader.readLine();
                builder.append(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } //Traitement du fichier
        try {
            JSONArray array = new JSONArray(builder.toString());
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                String name = object.getString("name");
                String image = object.getString("image");
                POKEMON_TYPE type1 = POKEMON_TYPE.valueOf(object.getString("type1"));
                POKEMON_TYPE type2 = null;
                if (object.has("type2"))
                    type2 = POKEMON_TYPE.valueOf(object.getString("type2"));
                //TO DO FINISH HERE
                int id = getResources().getIdentifier(image,
                        "drawable",
                        binding.getRoot().getContext().getPackageName());
                Pokemon pokemon = new Pokemon(i + 1, name, id, type1, type2); // Create Pokemon object
                pokemonList.add(pokemon);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void onClickOnPokemon(int position) {
        if (listener != null)
            listener.onClickOnPokemon(pokemonList.get(position));
    }

}