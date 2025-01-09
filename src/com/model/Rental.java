package com.model;

import java.util.*;

public class Rental {
    private int id;
    private Calendar dateStart;
    private Calendar dateEnd;
    private Client client;
    private ArrayList<Product> products;
    private double totalCost;

    public Rental(int id, Calendar dateStart, Calendar dateEnd, Client client, ArrayList<Product> products, double totalCost) {
        this.id = id;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.client = client;
        this.products = products;
        this.totalCost = totalCost;
    }

    public int id() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public Calendar dateStart() {
        return dateStart;
    }
    public void setDateStart(Calendar dateStart) {
        this.dateStart = dateStart;
    }
    public Calendar dateEnd() {
        return dateEnd;
    }
    public void setDateEnd(Calendar dateEnd) {
        this.dateEnd = dateEnd;
    }
    public Client client() {
        return client;
    }
    public void setClient(Client client) {
        this.client = client;
    }
    public ArrayList<Product> products() {
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
}
