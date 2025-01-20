package com.Controller;
import com.Controller.Services.ClientsService;
import com.Controller.Services.ProductsService;
import com.Controller.Services.RentalsService;
import com.model.Client;
import com.model.Product;
import com.model.Rental;
import com.dao.RentalsSQL;
import com.utils.Alert;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.controlsfx.control.textfield.TextFields;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

public class RentalsController {
    private static RentalsController instance;
    @FXML
    private TableView<Rental> rentalTable;
    @FXML
    private TableColumn<Rental, Integer> idColumn;
    @FXML
    private TableColumn<Rental, String> clientColumn;
    @FXML
    private TableColumn<Rental, Integer> productIdColumn;
    @FXML
    private TableColumn<Rental, String> productNameColumn;
    @FXML
    private TableColumn<Rental, Double> productPriceColumn;
    @FXML
    private TableColumn<Rental, String> dateStartColumn;
    @FXML
    private TableColumn<Rental, String> dateEndColumn;

    @FXML
    private ComboBox<String> cmbClient;
    @FXML
    private ListView<String> listViewProduct;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private Button addRentalBtn;

    @FXML
    public void initialize() {
        instance = this;
        setupTableColumns();
        loadTableData();
        setupClientAutocomplete();
        setupProductAutocomplete();

        ContextMenu contextMenu = new ContextMenu();
        MenuItem removeRental = new MenuItem("Remove rental");
        MenuItem removeProdFromRental = new MenuItem("Remove product from rental");

        contextMenu.getItems().add(removeRental);
        contextMenu.getItems().add(removeProdFromRental);

        rentalTable.setRowFactory( tv -> {
            TableRow<Rental> row = new TableRow<>();
            row.setOnContextMenuRequested(event -> {
                if (!row.isEmpty()) {
                    contextMenu.show(row, event.getScreenX(), event.getScreenY());
                }
            });
            return row;
        } );

        removeRental.setOnAction(e -> {
            Rental selectedRental = rentalTable.getSelectionModel().getSelectedItem();
            if(selectedRental != null && RentalsService.removeRental(selectedRental.getId())){
                rentalTable.refresh();
                Alert.showAlert("Success", "Rental deleted successfully.");
            }else{
                Alert.showAlert("Error", "Failed to delete rental.");
            }
        });

        removeProdFromRental.setOnAction(e->{
            Rental selectedRental = rentalTable.getSelectionModel().getSelectedItem();
            if(selectedRental != null && RentalsService.removeProdFromRental(selectedRental.getId(), selectedRental.getProductId())){
                rentalTable.refresh();
                Alert.showAlert("Success", "Product from rental deleted successfully.");
            }else{
                Alert.showAlert("Error", "Failed to delete product from rental.");
            }
        });

    }

    public static RentalsController getInstance(){
        return instance;
    }

    private void setupTableColumns() {
        idColumn.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getId()).asObject());

        clientColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getClientName()));

        productIdColumn.setCellValueFactory(cellData -> {
            try {
                int rowIndex = rentalTable.getItems().indexOf(cellData.getValue());
                ArrayList<Product> products = cellData.getValue().getProducts();  // Use the correct getter
                if (products != null && !products.isEmpty()) {
                    Product product = products.get(rowIndex % products.size());
                    if (product != null) {
                        return new SimpleIntegerProperty(product.getId()).asObject();
                    }
                }
            } catch (Exception e) {
                System.err.println("Error getting product ID: " + e.getMessage());
            }
            return new SimpleIntegerProperty(0).asObject();
        });

        productNameColumn.setCellValueFactory(cellData -> {
            try {
                int rowIndex = rentalTable.getItems().indexOf(cellData.getValue());
                ArrayList<Product> products = cellData.getValue().getProducts();  // Use the correct getter
                if (products != null && !products.isEmpty()) {
                    Product product = products.get(rowIndex % products.size());
                    if (product != null) {
                        return new SimpleStringProperty(product.getName());
                    }
                }
            } catch (Exception e) {
                System.err.println("Error getting product name: " + e.getMessage());
            }
            return new SimpleStringProperty("");
        });

        productPriceColumn.setCellValueFactory(cellData -> {
            try {
                int rowIndex = rentalTable.getItems().indexOf(cellData.getValue());
                ArrayList<Product> products = cellData.getValue().getProducts();  // Use the correct getter
                if (products != null && !products.isEmpty()) {
                    Product product = products.get(rowIndex % products.size());
                    if (product != null) {
                        return new SimpleDoubleProperty(product.getSellPrice()).asObject();
                    }
                }
            } catch (Exception e) {
                System.err.println("Error getting product price: " + e.getMessage());
            }
            return new SimpleDoubleProperty(0.0).asObject();
        });

        dateStartColumn.setCellValueFactory(cellData -> {
            Calendar date = cellData.getValue().getDateStart();
            if (date != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                return new SimpleStringProperty(sdf.format(date.getTime())); // Convierte Calendar a texto
            }
            return new SimpleStringProperty(""); // Para valores nulos, devuelve una cadena vacía
        });


        dateEndColumn.setCellValueFactory(cellData -> {
            Calendar date = cellData.getValue().getDateEnd();
            if (date != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                return new SimpleStringProperty(sdf.format(date.getTime()));
            }
            return new SimpleStringProperty("");
        });

        addRentalBtn.setOnAction( e -> addRental());
    }
    private void loadTableData() {
        // Load data only if necessary
        if (RentalsService.getRentals().isEmpty()) {
            ClientsService.loadClients();
            ProductsService.loadProducts();
            RentalsService.loadRentals();
        }

        ObservableList<Rental> expandedRentals = FXCollections.observableArrayList();

        // Iterate over all rentals and split them into individual rows for each product
        for (Rental rental : RentalsService.getRentals()) {
            ArrayList<Product> products = rental.getProducts();

            // If the rental contains products, add separate rows for each product
            if (products != null && !products.isEmpty()) {
                for (Product product : products) {
                    if (product != null) {
                        // Create a new Rental object with only one product
                        ArrayList<Product> singleProductList = new ArrayList<>();
                        singleProductList.add(product);

                        // Create a Rental row for this single product
                        Rental productRentalRow = new Rental(
                                rental.getId(),
                                rental.getDateStart(),
                                rental.getDateEnd(),
                                rental.getClient(),
                                singleProductList,
                                product.getSellPrice()
                        );

                        // Add to the table's observable list
                        expandedRentals.add(productRentalRow);
                    }
                }
            }
        }

        // Set the items for the rentalTable
        rentalTable.setItems(expandedRentals);
        rentalTable.refresh();
    }

    private void setupClientAutocomplete() {
        List<String> clientFullNames = ClientsService.getClient().stream()
                .map(client ->client.getId() + "- " + client.getFirstName() + " " + client.getLastName())
                .collect(Collectors.toList());

        cmbClient.setItems(FXCollections.observableArrayList(clientFullNames));

        cmbClient.setEditable(true);

        cmbClient.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            ObservableList<String> filteredList = FXCollections.observableArrayList(
                    clientFullNames.stream()
                            .filter(name -> name.toLowerCase().contains(newValue.toLowerCase()))
                            .collect(Collectors.toList())
            );
            cmbClient.setItems(filteredList);
            cmbClient.show();
        });
    }
    private void setupProductAutocomplete() {
        List<String> productsNames = ProductsService.getProducts().stream()
                .map(product -> product.getId() + " " + product.getName())
                .collect(Collectors.toList());

        listViewProduct.setItems(FXCollections.observableArrayList(productsNames));
        listViewProduct.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }


    private void addRental(){
        String selectedClient = cmbClient.getValue();
        ObservableList<String> selectedProducts = listViewProduct.getSelectionModel().getSelectedItems();
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        if (selectedClient == null || selectedProducts.isEmpty() || startDate == null || endDate == null) {
            throw new IllegalArgumentException("All fields must be filled.");
        }
        Client client = ClientsService.getClientById(Integer.parseInt(selectedClient.split("-")[0]));
        ArrayList<Product> products = new ArrayList<>();

        for(String prodString : selectedProducts){
            int id = Integer.parseInt(prodString.split(" ")[0]);
            Product product = ProductsService.getProductById(id);
            if (product != null) {
                products.add(product);
                System.out.println(product.getName());
            }
        }
        double totalCost = products.stream().mapToDouble(Product::getSellPrice).sum();

        Calendar startCal = Calendar.getInstance();
        startCal.set(startDate.getYear(), startDate.getMonthValue() - 1, startDate.getDayOfMonth());

        Calendar endCal = Calendar.getInstance();
        endCal.set(endDate.getYear(), endDate.getMonthValue() - 1, endDate.getDayOfMonth());

        int rentalId = RentalsService.addRental(client, products, startCal, endCal, totalCost);
        rentalTable.getItems().clear();
        ObservableList<Rental> rentalRows = FXCollections.observableArrayList();
        for (Product product : products) {
            ArrayList<Product> singleProductList = new ArrayList<>();
            singleProductList.add(product);
            SimpleDateFormat format = new SimpleDateFormat("DD/MM/YYYY");
            Rental productRentalRow = new Rental(
                    rentalId,
                    startCal,
                    endCal,
                    client,
                    singleProductList,
                    product.getSellPrice()
            );
            rentalRows.add(productRentalRow);
        }
        rentalTable.setItems(rentalRows);
        rentalTable.refresh();
    }

    public void showClientRentals(int clientId){
        ObservableList<Rental> rentals = FXCollections.observableArrayList(
                RentalsService.getRentals().stream()
                        .filter(r -> r.getClient().getId() == clientId)
                        .collect(Collectors.toList())
        );
        rentalTable.getItems().clear();
        ObservableList<Rental> rentalRows = FXCollections.observableArrayList();
        for(Rental r : rentals){
            for (Product product : r.getProducts()) {
                ArrayList<Product> singleProductList = new ArrayList<>();
                singleProductList.add(product);
                SimpleDateFormat format = new SimpleDateFormat("DD/MM/YYYY");
                Rental productRentalRow = new Rental(
                        r.getId(),
                        r.getDateStart(),
                        r.getDateEnd(),
                        r.getClient(),
                        singleProductList,
                        product.getSellPrice()
                );
                rentalRows.add(productRentalRow);
            }
            rentalTable.setItems(rentalRows);
            rentalTable.refresh();
        }

    }

}