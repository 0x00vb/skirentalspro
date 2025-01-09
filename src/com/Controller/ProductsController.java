package com.Controller;

import com.dao.ProductSQL;
import com.model.Product;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.ArrayList;

public class ProductsController {
    @FXML
    private TableView<Product> productTable;

    @FXML
    private TableColumn<Product, Integer> idColumn;
    @FXML
    private TableColumn<Product, String> nameColumn;
    @FXML
    private TableColumn<Product, String> brandColumn;
    @FXML
    private TableColumn<Product, String> categoryColumn;
    @FXML
    private TableColumn<Product, Double> costPriceColumn;
    @FXML
    private TableColumn<Product, Double> sellPriceColumn;

    @FXML
    private TextField prodNameTxt, prodBrandTxt, prodCatTxt, prodCostTxt, prodSellTxt, prodIdTxt;

    @FXML
    private Button addProdBtn, searchProdBtn;

    private ObservableList<Product> products = FXCollections.observableArrayList();
    private final ProductSQL productSQL = new ProductSQL();

    @FXML
    public void initialize() {
        // Map table columns to Product properties
        idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        brandColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBrand()));
        categoryColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCategory()));
        costPriceColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getCostPrice()).asObject());
        sellPriceColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getSellPrice()).asObject());

        // Load initial data
        products.addAll(productSQL.loadProducts());
        productTable.setItems(products); // causes error


        // Set button actions
        addProdBtn.setOnAction(e -> addProduct());
        searchProdBtn.setOnAction(e -> searchProduct());
    }

    private void addProduct() {
        try {
            String name = prodNameTxt.getText();
            String brand = prodBrandTxt.getText();
            String category = prodCatTxt.getText();
            double costPrice = Double.parseDouble(prodCostTxt.getText());
            double sellPrice = Double.parseDouble(prodSellTxt.getText());

            Product newProduct = new Product(0, name, brand, category, costPrice, sellPrice);
            if (productSQL.addProduct(newProduct)) {
                productSQL.loadProducts();
                clearInputs();
                showAlert("Success", "Product added successfully.");
            } else {
                showAlert("Error", "Failed to add product.");
            }
        } catch (Exception e) {
            showAlert("Error", "Invalid input data.");
        }
    }

    private void clearInputs() {
        prodNameTxt.clear();
        prodBrandTxt.clear();
        prodCatTxt.clear();
        prodCostTxt.clear();
        prodSellTxt.clear();
        prodIdTxt.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void searchProduct() {
        String searchId = prodIdTxt.getText().trim();

        if (searchId.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Input Required");
            alert.setContentText("Please enter a product ID to search.");
            alert.showAndWait();
            return;
        }

        try {
            int id = Integer.parseInt(searchId);

            // Search in the in-memory list
            Product foundProduct = products.stream()
                    .filter(product -> product.getId() == id)
                    .findFirst()
                    .orElse(null);

            if (foundProduct != null) {
                // Display the found product in the table
                ObservableList<Product> searchResult = FXCollections.observableArrayList(foundProduct);
                productTable.setItems(searchResult);
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Product Not Found");
                alert.setHeaderText(null);
                alert.setContentText("No product found with ID: " + id);
                alert.showAndWait();
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Input");
            alert.setHeaderText("ID must be a number");
            alert.setContentText("Please enter a valid numeric ID.");
            alert.showAndWait();
        }
    }
}
