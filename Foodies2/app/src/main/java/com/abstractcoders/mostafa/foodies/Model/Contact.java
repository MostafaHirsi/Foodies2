package com.abstractcoders.mostafa.foodies.Model;

public class Contact {

    private String displayName;
    private String email;

    public Contact (String name, String email) {
        this.displayName = name;
        this.email = email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getEmail() {
        return email;
    }

}