package com.example.fruit_tourney;

public class Fruit {
    private String fid;
    private String label;
    private String image;

    public Fruit() { }

    public Fruit(String id, String name, String img) {
        this.fid = id;
        this.label = name;
        this.image = img;
    }

    // --- GETTERS ---
    public String getFid() { return fid; }
    public String getLabel() { return label; }
    public String getImage() { return image; }

    // --- SETTERS ---
    public void setLabel(String name) { this.label = name; }
    public void setFid(String id) { this.fid = id; }
    public void setImage(String img) { this.image = img; }
}
