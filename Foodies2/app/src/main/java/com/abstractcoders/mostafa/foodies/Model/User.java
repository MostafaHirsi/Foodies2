package com.abstractcoders.mostafa.foodies.Model;

/**
 * Created by Mostafa on 28/02/2016.
 */
public class User {
    String username, firstName, secondName, address, password;
    byte[] photo;

    public User(String username, String firstName, String secondName, String address, String password, byte[] photo) {
        this.username = username;
        this.firstName = firstName;
        this.secondName = secondName;
        this.address = address;
        this.password = password;
        this.photo = photo;
    }
    
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }
    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public byte[] getPhoto() {
        return photo;
    }
    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }
}
