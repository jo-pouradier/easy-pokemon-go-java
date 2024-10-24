package com.example.pokemongeo_tp;

import com.example.pokemongeo_tp.entities.ItemEntity;


public class Item {
    String name;
    int resource;
    String image;
    int id;
    int quantity;
    public Item() {
        this.id = 1;
        name = "Unknown";
        resource = R.drawable.normal;
        }
    public Item(String name, int resource, String image, int id,int quantity) {
        this.name = name;
        this.resource = resource;
        this.image = image;
        this.id = id;
        this.quantity = quantity;
    }

    public Item(ItemEntity item) {
        this.name = item.name;
        this.image = item.image;
        this.id = item.item_id;
        this.quantity = item.quantity;
    }
    public String getName(){
        return name;
    }

    public int getQuantity(){
        return quantity;
    }

}
