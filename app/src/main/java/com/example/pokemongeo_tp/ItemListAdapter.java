package com.example.pokemongeo_tp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.example.pokemongeo_tp.databinding.ItemItemBinding;

import java.util.List;

public class ItemListAdapter extends
        RecyclerView.Adapter<ItemListAdapter.ViewHolder> {

    List<Item> itemList;


    public ItemListAdapter(List<Item> itemList) {
        assert itemList != null;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_item, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.viewModel.setItem(item);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemItemBinding binding;
        private final ItemViewModel viewModel = new ItemViewModel();

        ViewHolder(ItemItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.setItemViewModel(viewModel);

        }
    }
}