package com.example.madproject2;

import java.util.HashMap;

public class UserDatabase {
    private static final HashMap<String, HashMap<String, String>> usersDB = new HashMap<>();

    public static boolean userExists(String username) {
        return usersDB.containsKey(username);
    }

    public static void registerUser(String username, HashMap<String, String> userData) {
        usersDB.put(username, userData);
    }

    public static HashMap<String, String> getUser(String username) {
        return usersDB.get(username);
    }

    public static boolean validateLogin(String username, String password) {
        if (!usersDB.containsKey(username)) return false;
        return usersDB.get(username).get("password").equals(password);
    }
}
