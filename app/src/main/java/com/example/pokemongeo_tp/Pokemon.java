package com.example.pokemongeo_tp;


import com.example.pokemongeo_tp.entities.PokemonEntity;
import com.example.pokemongeo_tp.enums.POKEMON_TYPE;

public class Pokemon {
    private int order;
    private String name;
    private int height;
    private int weight;
    private int frontResource;
    private POKEMON_TYPE type1;
    private int type1Resource;
    private POKEMON_TYPE type2;
    private int type2Ressource;
    private boolean discovered;


    public Pokemon() {
        this.order = 1;
        name = "Unknown";
        frontResource = R.drawable.normal;
        type1 = POKEMON_TYPE.Unknown;
    }

    public Pokemon(int order) {
        this.order = order;
        name = "Unknown";
        frontResource = R.drawable.normal;
        type1 = POKEMON_TYPE.Unknown;
    }

    public Pokemon(PokemonEntity poke) {
        this.order = poke.id;
        this.name = poke.name;
        this.type1 = POKEMON_TYPE.valueOf(poke.type_1);
        this.discovered = poke.discovered;

        try {
            this.type1Resource = R.drawable.class.getDeclaredField(poke.type_1.toLowerCase()).getInt(null);
        } catch (Exception e) {
            this.type1Resource = R.drawable.feu;
        }
        try {
            if (poke.type_2 != null) {
                this.type2 = POKEMON_TYPE.valueOf(poke.type_2);
                this.type2Ressource = R.drawable.class.getDeclaredField(poke.type_2.toLowerCase()).getInt(null);
            } else {
                this.type2 = null;
                this.type2Ressource = R.drawable.normal;
            }
        } catch (Exception e) {
            this.type2Ressource = R.drawable.normal;
        }
        try {
            this.frontResource = R.drawable.class.getDeclaredField(poke.image).getInt(null);
        } catch (Exception e) {
            this.frontResource = R.drawable.normal;
        }

    }

    public Pokemon(int order, String name, int frontResource,
                   POKEMON_TYPE type1, int type1Resource, POKEMON_TYPE type2, int type2Ressource) {
        this.order = order;
        this.name = name;
        this.frontResource = frontResource;
        this.type1 = type1;
        this.type1Resource = type1Resource;
        this.type2 = type2;
        this.type2Ressource = type2Ressource;
        discovered = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getFrontResource() {

        return frontResource;
    }

    public void setFrontResource(int frontResource) {
        this.frontResource = frontResource;
    }

    public POKEMON_TYPE getType1() {
        return type1;
    }

    public void setType1(POKEMON_TYPE type1) {
        this.type1 = type1;
    }

    public int getType1Resource() {
        return type1Resource;
    }

    public void setType1Resource(int type1Resource) {
        this.type1Resource = type1Resource;
    }

    public POKEMON_TYPE getType2() {
        return type2;
    }

    public void setType2(POKEMON_TYPE type2) {
        this.type2 = type2;
    }

    public int getType2Ressource() {
        return type2Ressource;
    }

    public void setType2Ressource(int type2Ressource) {
        this.type2Ressource = type2Ressource;
    }

    public String getType1String() {
        return type1.name();
    }

    public String getType2String() {
        return type2.name();
    }

    public boolean isDiscovered() {
        return discovered;
    }

    public void setDiscovered(boolean discovered) {
        this.discovered = discovered;
    }
}


