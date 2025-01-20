package com.model;

import java.sql.Date;
import java.util.*;

public class Rental {
    private int id;
    private Calendar dateStart;
    private Calendar dateEnd;
    private Client client;
    private ArrayList<Product> products;
    private double totalCost;

    public Rental(){}

    public Rental(int id, Calendar dateStart, Calendar dateEnd, Client client, ArrayList<Product> products, double totalCost) {
        this.id = id;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.client = client;
        this.products = products;
        this.totalCost = totalCost;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public Calendar getDateStart() {
        return dateStart;
    }
    public void setDateStart(Calendar dateStart) {
        this.dateStart = dateStart;
    }
    public Calendar getDateEnd() {
        return dateEnd;
    }
    public void setDateEnd(Calendar dateEnd) {
        this.dateEnd = dateEnd;
    }
    public Client getClient() {
        return client;
    }
    public void setClient(Client client) {
        this.client = client;
    }
    public ArrayList<Product> getProducts() {
        return products;
    }
    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }
    public double getTotalCost(){
        return this.totalCost;
    }
    public void setTotalCost(double totalCost){
        this.totalCost = totalCost;
    }
    public String getClientName() {
        return client != null ? client.getFirstName() + " " + client.getLastName() : "";
    }
    public Integer getProductId() {
        return !products.isEmpty() ? products.get(0).getId() : null;
    }
    public String getProductName() {
        return !products.isEmpty() ? products.get(0).getName() : "";
    }
    public Double getProductPrice() {
        return !products.isEmpty() ? products.get(0).getSellPrice() : null;
    }
}
