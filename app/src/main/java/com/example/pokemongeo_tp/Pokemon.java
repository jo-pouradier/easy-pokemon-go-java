package com.example.pokemongeo_tp;


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

    public Pokemon() {
        order = 1;
        name = "Unknown";
        frontResource = R.drawable.p1;
        type1 = POKEMON_TYPE.Plante;
    }
    public Pokemon(int order, String name, int frontResource,
                   POKEMON_TYPE type1,int type1Resource, POKEMON_TYPE type2, int type2Ressource) {
        this.order = order;
        this.name = name;
        this.frontResource = frontResource;
        this.type1 = type1;
        this.type1Resource = type1Resource;
        this.type2 = type2;
        this.type2Ressource = type2Ressource;
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
    public void setType1Resource(int type1Resource) {
        this.type1Resource = type1Resource;
    }
    public int getType1Resource() {
        return type1Resource;
    }
    public POKEMON_TYPE getType2() {
        return type2;
    }
    public void setType2(POKEMON_TYPE type2) {
        this.type2 = type2;
    }
    public void setType2Ressource(int type2Ressource) {
        this.type2Ressource = type2Ressource;
    }
    public int getType2Ressource() {
        return type2Ressource;
    }
    public String getType1String() {
        return type1.name();
    }
    public String getType2String() {
        return type2.name();
    }
}


