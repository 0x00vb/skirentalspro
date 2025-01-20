package com.Controller;

import com.Controller.Services.ClientsService;
import com.Controller.Services.ProductsService;
import com.Main;
import com.model.Client;
import com.model.Product;
import com.utils.Alert;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class ClientsController {
    @FXML
    private TableView<Client> clientTable;
    @FXML
    private Button addClientBtn;

    @FXML
    private TableColumn<Client, Integer> idColumn;
    @FXML
    private TableColumn<Client, String> firstNameColumn;
    @FXML
    private TableColumn<Client, String> lastNameColumn;
    @FXML
    private TableColumn<Client, String> phoneNumberColumn;
    @FXML
    private TableColumn<Client, String> addressColumn;

    @FXML
    private TextField firstNameTxt;
    @FXML
    private TextField lastNameTxt;
    @FXML
    private TextField phoneNumberTxt;
    @FXML
    private TextField addressTxt;


    private ObservableList<Product> products = FXCollections.observableArrayList();

    @FXML
    public void initialize(){
        idColumn.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        firstNameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty((cellData.getValue().getFirstName())));
        lastNameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty((cellData.getValue().getLastName())));
        phoneNumberColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty((cellData.getValue().getPhoneNumber())));
        addressColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getAddress()));

        if (ClientsService.getClient().isEmpty()) {
            ClientsService.loadClients();
        }
        clientTable.setItems(ClientsService.getClient());

        // Set button actions
        addClientBtn.setOnAction(e -> addClient());

        ContextMenu contextMenu = new ContextMenu();
        MenuItem removeClient = new MenuItem("Remove");
        MenuItem viewRentalsItem = new MenuItem("View Rentals");
        contextMenu.getItems().add(viewRentalsItem);
        contextMenu.getItems().add(removeClient);

        clientTable.setRowFactory(tv -> {
            TableRow<Client> row = new TableRow<>();
            row.setOnContextMenuRequested(event -> {
                if (!row.isEmpty()) {
                    contextMenu.show(row, event.getScreenX(), event.getScreenY());
                }
            });
            return row;
        });

        viewRentalsItem.setOnAction(e -> {
            Client selectedClient = clientTable.getSelectionModel().getSelectedItem();
            if (selectedClient != null) {
                showRentals(selectedClient.getId());
            } else {
                Alert.showAlert("No Client Selected", "Please select a client to view their rentals.");
            }
        });

        removeClient.setOnAction(e -> {
            Client selectedClient = clientTable.getSelectionModel().getSelectedItem();
            if(selectedClient != null && ClientsService.removeClient(selectedClient.getId())){
                clientTable.refresh();
                Alert.showAlert("Success", "Client deleted successfully.");
            }else{
                Alert.showAlert("Error", "Failed to delete client.");
            }
        });
    }

    public void addClient(){
        String firstName = firstNameTxt.getText().trim();
        String lastName = lastNameTxt.getText().trim();
        String phoneNumber = phoneNumberTxt.getText().trim();
        String address = addressTxt.getText().trim();
        if (firstName.isEmpty() || lastName.isEmpty() || phoneNumber.isEmpty() || address.isEmpty()) {
            Alert.showAlert("Validation Error", "All fields are required. Please fill them out.");
            return;
        }
        if(ClientsService.addClient(firstName, lastName, phoneNumber, address)){
            Alert.showAlert("Success", "Client added successfully.");
            clientTable.refresh();
        }
    }

    public void showRentals(int clientId){
        MainController.getInstance().loadSection("../resources/rentalsPane.fxml", clientId);
    }

    public void clearFields(){
        firstNameTxt.clear();
        lastNameTxt.clear();
        phoneNumberTxt.clear();
        addressTxt.clear();
    }
}
