package com.Controller;

import com.Controller.Services.ProductsService;
import com.Controller.Services.RentalsService;
import com.dao.ProductSQL;
import com.model.Product;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.ArrayList;

import static com.utils.Alert.showAlert;

public class ProductsController {
    @FXML
    private TableView<Product> productTable;

    @FXML
    private TableColumn<Product, Integer> idColumn;
    @FXML
    private TableColumn<Product, String> nameColumn, brandColumn, categoryColumn;
    @FXML
    private TableColumn<Product, Boolean> rentedColumn;
    @FXML
    private TableColumn<Product, Double> costPriceColumn;
    @FXML
    private TableColumn<Product, Double> sellPriceColumn;

    @FXML
    private TextField prodNameTxt, prodBrandTxt, prodCatTxt, prodCostTxt, prodSellTxt, prodIdTxt;

    @FXML
    private Button addProdBtn, searchProdBtn;

    private ObservableList<Product> products = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Map table columns to Product properties
        idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        brandColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBrand()));
        categoryColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCategory()));
        costPriceColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getCostPrice()).asObject());
        sellPriceColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getSellPrice()).asObject());
        rentedColumn.setCellValueFactory( cellData -> new SimpleBooleanProperty(cellData.getValue().getIsRented()));

        if (ProductsService.getProducts().isEmpty()) {
            ProductsService.loadProducts();
        }

        // Set the rentals list to the table
        productTable.setItems(ProductsService.getProducts());

        // Set button actions
        addProdBtn.setOnAction(e -> addProduct());
        searchProdBtn.setOnAction(e -> searchProduct());

        // Create context menu
        ContextMenu contextMenu = new ContextMenu();
        MenuItem deleteItem = new MenuItem("Delete");
        contextMenu.getItems().add(deleteItem);

        // Set up row factory for the table
        productTable.setRowFactory(tv -> {
            TableRow<Product> row = new TableRow<>();

            // Only show context menu on rows with data
            row.setOnContextMenuRequested(event -> {
                if (!row.isEmpty()) {
                    contextMenu.show(row, event.getScreenX(), event.getScreenY());
                }
            });

            // Optional: Also handle double click
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    contextMenu.show(row, event.getScreenX(), event.getScreenY());
                }
            });

            return row;
        });

        // Set the delete action
        deleteItem.setOnAction(e -> {
            Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
            if (selectedProduct != null) {
                if (ProductSQL.deleteProduct(selectedProduct.getId())) {
                    ProductsService.getProducts().remove(selectedProduct);
                    productTable.refresh();
                    showAlert("Success", "Product deleted successfully.");
                } else {
                    showAlert("Error", "Failed to delete product.");
                }
            }
        });
    }

    private void addProduct() {
        try {
            String name = prodNameTxt.getText();
            String brand = prodBrandTxt.getText();
            String category = prodCatTxt.getText();
            double costPrice = Double.parseDouble(prodCostTxt.getText());
            double sellPrice = Double.parseDouble(prodSellTxt.getText());

            if (ProductsService.addProduct(name, brand, category, costPrice, sellPrice)) {
                clearInputs();
                productTable.refresh();
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

            Product foundProduct = ProductsService.getProducts().stream()
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
