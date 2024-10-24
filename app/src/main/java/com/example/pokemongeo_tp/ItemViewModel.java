package com.example.pokemongeo_tp;

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

    @Bindable
    public  String getQuantity(){ return "X " + item.getQuantity();   }
}