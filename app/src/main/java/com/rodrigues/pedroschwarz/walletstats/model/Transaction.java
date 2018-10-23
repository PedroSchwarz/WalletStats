package com.rodrigues.pedroschwarz.walletstats.model;

public class Transaction {

    private String id;
    private String desc;
    private String category;
    private Double amount;
    private String date;
    private String type;

    public Transaction() {
    }

    public Transaction(String id, String desc, String category, Double amount, String date, String type) {
        this.id = id;
        this.desc = desc;
        this.category = category;
        this.amount = amount;
        this.date = date;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
