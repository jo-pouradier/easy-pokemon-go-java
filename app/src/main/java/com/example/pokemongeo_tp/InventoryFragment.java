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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.pokemongeo_tp.database.Database;
import com.example.pokemongeo_tp.databinding.InventoryFragmentBinding;
import com.example.pokemongeo_tp.entities.ItemEntity;
import com.example.pokemongeo_tp.threading.RequestPromise;
import com.example.pokemongeo_tp.threading.RequestThread;
import com.example.pokemongeo_tp.threading.ThreadEventListener;

import java.util.ArrayList;
import java.util.List;

public class InventoryFragment extends Fragment {

    List<Item> itemList = new ArrayList<>();
    ItemListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        InventoryFragmentBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.inventory_fragment, container, false);
        binding.itemList.setLayoutManager(new LinearLayoutManager(
                binding.getRoot().getContext()));

        itemList = createItemList(binding);
        adapter = new ItemListAdapter(itemList);
        binding.itemList.setAdapter(adapter);
        return binding.getRoot();
    }

    public List<Item> createItemList(InventoryFragmentBinding binding) {
        // Ouverture du fichier res/raw
        List<Item> ItemList = new ArrayList<>();
        RequestPromise<Context, List<ItemEntity>> promise = new RequestPromise<>(
                new ThreadEventListener<List<ItemEntity>>() {
                    @Override
                    public void OnEventInThread(List<ItemEntity> data) {
                        for (ItemEntity item : data) {
                            // map pokemon with pokemonEntity
                            ItemList.add(new Item(item));
                        }
                        // refresh view
                        new Handler(Looper.getMainLooper()).post(() -> {
                            binding.itemList.setAdapter(adapter);
                        });
                    }

                    @Override
                    public void OnEventInThreadReject(String error) {
                        Log.e("ERROR", "while creating pokemon from PokemonEntity. " + error);
                    }
                },
                (Context context) -> {
                    Database db = Database.getInstance(context);
                    return db.itemDao().getAll();
                },
                requireContext()
        );
        RequestThread instance = RequestThread.getInstance();
        instance.addRequest(promise);

        return ItemList;
    }
}
