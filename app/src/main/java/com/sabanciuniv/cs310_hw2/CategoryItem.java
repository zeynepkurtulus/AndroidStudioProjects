package com.sabanciuniv.cs310_hw2;

import androidx.annotation.NonNull;

public class CategoryItem {
    private int id;
    private String name;

    public CategoryItem(){ //default conts

    }
    public CategoryItem(int i, String n){
        this.id = i;
        this.name = n;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NonNull
    @Override
    public String toString() {
        return "CategoryItem{" +
                "name='" + name + '\'' +
                '}';
    }
}
