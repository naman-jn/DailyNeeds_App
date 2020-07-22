package com.njain.dailyneeds.model;

public class Item {
    private int id;
    private String name;
    private int qty;
    private String description;
    private String dateItemAdded;

    public Item(){

    }

    public Item(int id, String name, int qty, String description, String dateItemAdded) {
        this.id = id;
        this.name = name;
        this.qty = qty;
        this.description = description;
        this.dateItemAdded = dateItemAdded;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDateItemAdded() {
        return dateItemAdded;
    }

    public void setDateItemAdded(String dateItemAdded) {
        this.dateItemAdded = dateItemAdded;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "id=" + id +
                ", name='" + name + '\'' +
                ", qty=" + qty +
                ", description='" + description + '\'' +
                ", dateItemAdded='" + dateItemAdded + '\'' +
                '}';
    }
}
