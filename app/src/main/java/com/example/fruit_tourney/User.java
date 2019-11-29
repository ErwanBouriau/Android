package com.example.fruit_tourney;

public class User {
    private String uid;
    private String username;
    private String email;

    public User() { }

    public User(String uid, String username, String mail) {
        this.uid = uid;
        this.username = username;
        this.email = mail;
    }

    // --- GETTERS ---
    public String getUid() { return uid; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }

    // --- SETTERS ---
    public void setUsername(String username) { this.username = username; }
    public void setUid(String uid) { this.uid = uid; }
    public void setEmail(String mail) { this.email = mail; }
}
