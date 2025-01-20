package com.Controller.Services;

import com.dao.ClientsSQL;
import com.dao.RentalsSQL;
import com.model.Client;
import com.model.Rental;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class ClientsService {
    private static ObservableList<Client> clients = FXCollections.observableArrayList();
    // Method to load rentals (from database or other source)
    public static void loadClients() {
        clients.setAll(ClientsSQL.loadClients());
    }

    // Getter for the rentals list
    public static ObservableList<Client> getClient() {
        return clients;
    }

    public static Client getClientById(int id){
        for(Client c : clients){
            if(c.getId() == id){
                return c;
            }
        }
        return null;
    }

    public static boolean addClient(String firstName, String lastName, String phoneNumber, String address){
        int clientAddedDB = ClientsSQL.addClient(firstName, lastName, phoneNumber, address);
        if(clientAddedDB != -1){
            Client c = new Client(clientAddedDB, firstName, lastName, phoneNumber, address);
            clients.add(c);
            return  true;
        }
        return false;
    }

    public static boolean removeClient(int id){
        if(ClientsSQL.removeClient(id)){
            clients.removeIf(client -> client.getId() == id);
            return true;
        }
        return false;
    }
}
