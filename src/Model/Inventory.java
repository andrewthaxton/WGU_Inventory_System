package Model;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Inventory Class
 * This class is composed of primarily static members and used to hold the Inventory's data.
 */

public class Inventory {

    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     * Adds a new part to the Inventory
     * @param newPart The part to be added
     */

    public static void addPart(Part newPart) {
        allParts.add(newPart);
    }

    /**
     * Adds a new product to the Inventory
     * @param newProduct The product to be added
     */

    public static void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }

    /**
     * Searches by Part ID
     * @param partId the part ID
     * @return The matching part
     */

    public static Part lookupPart(int partId) {
        for (int index = 0; index < allParts.size(); index++) {
            Part searchPart = allParts.get(index);
            if (searchPart.getId() == partId) {
                return searchPart;
            }
        }
        return null;
    }

    /**
     * Searches by Product ID
     * @param productId the product ID
     * @return the matching product
     */

    public static Product lookupProduct(int productId) {
        for (int index = 0; index < allProducts.size(); index++) {
            Product searchProduct = allProducts.get(index);
            if(searchProduct.getId() == productId) {
                return searchProduct;
            }
        }
        return null;
    }

    /**
     * Searches by part name
     * @param partName the name (or piece of the name) of the part
     * @return list of matching parts
     */

    public static ObservableList<Part> lookupPart(String partName) {
        ObservableList<Part> foundParts = FXCollections.observableArrayList();
        for (int index = 0; index < allParts.size(); index++) {
            Part searchPart = allParts.get(index);
            if(searchPart.getName().toLowerCase().contains(partName.toLowerCase())) {
                foundParts.add(searchPart);
            }
        }
        return foundParts;
    }

    /**
     * Searches by product name
     * @param productName the name (or piece of the name) of the product
     * @return list of matching products
     */

    public static ObservableList<Product> lookupProduct(String productName) {
        ObservableList<Product> foundProducts = FXCollections.observableArrayList();
        for (int index = 0; index < allProducts.size(); index++) {
            Product searchProduct = allProducts.get(index);
            if(searchProduct.getName().toLowerCase().contains(productName.toLowerCase())) {
                foundProducts.add(searchProduct);
            }
        }
        return foundProducts;
    }

    /**
     * Updates a part's values
     * @param index the index of the part to be updated
     * @param selectedPart the updated version of the part
     */

    public static void updatePart(int index, Part selectedPart) {
        allParts.set(index, selectedPart);
    }

    /**
     * Updates a product's values
     * @param index the index of the product to be updated
     * @param newProduct the updated version of the product
     */

    public static void updateProduct(int index, Product newProduct) {
        allProducts.set(index, newProduct);
    }

    /**
     * Deletes a part from the Inventory
     * @param selectedPart the part to be deleted
     * @return true if part was removed
     */

    public static boolean deletePart(Part selectedPart) {
        return allParts.remove(selectedPart);
    }

    /**
     * Deletes a product from the Inventory
     * @param selectedProduct the product to be deleted
     * @return true if product was removed
     */

    public static boolean deleteProduct(Product selectedProduct) {
        return allProducts.remove(selectedProduct);
    }

    /**
     * Shows list of all parts in the Inventory.
     * @return All parts
     */

    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    /**
     * Shows list of all products in the Inventory.
     * @return All products
     */

    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }

    /**
     * Used to open the next window.
     * @param event the event that triggers the next screen
     * @param location the location of the next screen
     */

    public void setStage(ActionEvent event, String location) throws IOException {
        Stage stage;
        Parent scene;
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource(location));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * This creates an exception that I use throughout the code when the stock numbers are not valid.
     */

    public class stockErrorException extends Exception{
        public stockErrorException() {
            super();
        }
    }
}
