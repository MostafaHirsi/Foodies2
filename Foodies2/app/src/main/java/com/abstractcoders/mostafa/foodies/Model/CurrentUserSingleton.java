package com.abstractcoders.mostafa.foodies.Model;

/**
 * Created by Mostafa on 28/02/2016.
 */
public class CurrentUserSingleton {

    private static CurrentUserSingleton instance = null;
    private int userID;
    public CurrentUserSingleton(int userID) {
        this.userID = userID;
    }
    public CurrentUserSingleton() {

    }

    public static CurrentUserSingleton getInstance(int userID)
    {
        if(instance == null) {
            instance = new CurrentUserSingleton(userID);
        }
        return instance;
    }

    public static CurrentUserSingleton getInstance()
    {
        if(instance == null) {
            instance = new CurrentUserSingleton();
        }
        return instance;
    }
    public int getUserName() {
        return userID;
    }

    public void setUserName(int userID) {
        this.userID = userID;
    }

    public static void setInstance(CurrentUserSingleton instance) {
        CurrentUserSingleton.instance = instance;
    }
}
