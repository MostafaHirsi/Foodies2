package com.abstractcoders.mostafa.foodies.Model;

/**
 * Created by Mostafa on 27/02/2016.
 */
public class EateryType {
    String type;
    String name;
    boolean chosen;

    public EateryType(String type, String name, boolean chosen) {
        this.type = type;
        this.name = name;
        this.chosen = chosen;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChosen() {
        return chosen;
    }

    public void setChosen(boolean chosen) {
        this.chosen = chosen;
    }
}
