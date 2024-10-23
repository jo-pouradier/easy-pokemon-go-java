package com.example.pokemongeo_tp;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class ItemViewModel extends BaseObservable {
    private Item item = new Item();

    public void setItem(Item item) {
        this.item = item;
        notifyChange();
    }
    @Bindable
    public String getName() {return item.getName();
    }
}