package com.benrostudios.bakingapp.network.response;

import java.io.Serializable;

public class IngredientsBean implements Serializable {
    /**
     * quantity : 2
     * measure : CUP
     * ingredient : Graham Cracker crumbs
     */

    public IngredientsBean(double mQuantity, String mMeasure, String mIngredient) {
        quantity =mQuantity;
        measure = mMeasure;
        ingredient = mIngredient;
    }


    private double quantity;
    private String measure;
    private String ingredient;

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }
}
