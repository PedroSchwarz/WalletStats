package com.rodrigues.pedroschwarz.walletstats.model;

public class User {

    private String id;
    private String name;
    private Double rAmount;
    private Double eAmount;

    public User() {
    }

    public User(String id, String name, Double rAmount, Double eAmount) {
        this.id = id;
        this.name = name;
        this.rAmount = rAmount;
        this.eAmount = eAmount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getrAmount() {
        return rAmount;
    }

    public void setrAmount(Double rAmount) {
        this.rAmount = rAmount;
    }

    public Double geteAmount() {
        return eAmount;
    }

    public void seteAmount(Double eAmount) {
        this.eAmount = eAmount;
    }
}
