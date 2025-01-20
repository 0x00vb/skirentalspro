package com.model;

public class Product {
    private int id;
    private String name;
    private String brand;
    private String category;
    private double costPrice;
    private double sellPrice;
    private boolean rented;

    public Product(int id, String name, String brand, String category, double costPrice, double sellPrice, boolean rented) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.category = category;
        this.costPrice = costPrice;
        this.sellPrice = sellPrice;
        this.rented = rented;
    }

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public String getBrand() {return brand;}
    public void setBrand(String brand) {this.brand = brand;}
    public String getCategory() {return category;}
    public void setCategory(String category) {this.category = category;}
    public double getCostPrice() {return costPrice;}
    public void setCostPrice(double costPrice) {this.costPrice = costPrice;}
    public double getSellPrice() {return sellPrice;}
    public void setSellPrice(double sellPrice) {this.sellPrice = sellPrice;}
    public boolean getIsRented(){return this.rented;}
    public void setIsRented(boolean rented){this.rented = rented;}
}
