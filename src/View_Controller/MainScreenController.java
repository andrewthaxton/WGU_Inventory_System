package View_Controller;

import Model.Inventory;
import Model.Part;
import Model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * The MainScreenController class inherits from Inventory and is the controller for the MainScreen.fxml page.
 */

public class MainScreenController extends Inventory implements Initializable {

    @FXML
    private Label mainLabel;

    @FXML
    private Label partsLabel;

    @FXML
    private TableView<Part> partsTable;

    @FXML
    private TableColumn<?, ?> partIdCol;

    @FXML
    private TableColumn<?, ?> partNameCol;

    @FXML
    private TableColumn<?, ?> partInventoryCol;

    @FXML
    private TableColumn<?, ?> partPriceCol;

    @FXML
    private Button addPartButton;

    @FXML
    private Button modifyPartButton;

    @FXML
    private Button deletePartButton;

    @FXML
    private TextField mainSearchPartText;

    @FXML
    private Label productsLabel;

    @FXML
    private TableView<Product> productsTable;

    @FXML
    private TableColumn<?, ?> productIdCol;

    @FXML
    private TableColumn<?, ?> productNameCol;

    @FXML
    private TableColumn<?, ?> productInventoryCol;

    @FXML
    private TableColumn<?, ?> productPriceCol;

    @FXML
    private Button deleteProductButton;

    @FXML
    private Button modifyProductButton;

    @FXML
    private Button addProductButton;

    @FXML
    private TextField mainSearchProductText;

    @FXML
    private Button exitButton;

    Stage stage;
    Parent scene;


    /**
     * This initializes the page to have two tables. One containing all the Parts in Inventory and the other
     * containing all the Products in Inventory.
     * @param url
     * @param resourceBundle
     */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        partsTable.setItems(Inventory.getAllParts());
        productsTable.setItems(Inventory.getAllProducts());

        partIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInventoryCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        productIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInventoryCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /**
     * The onAddPartButton opens the AddPart.fxml page and runs the setIdField method.
     * @param event
     * @throws IOException
     */

    @FXML
    void onAddPartButton(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("AddPart.fxml"));
        loader.load();
        AddPartController addController = loader.getController();
        addController.setIdField();
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * The onAddProductButton opens the AddProduct.fxml screen and runs the setIdField method.
     * @param event
     * @throws IOException
     */

    @FXML
    void onAddProductButton(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("AddProduct.fxml"));
        loader.load();
        AddProductController addController = loader.getController();
        addController.setIdField();
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * The onDeletePartButton opens a confirmation alert that if selected will delete the selected part from Inventory.
     * @param event
     */

    @FXML
    void onDeletePartButton(ActionEvent event) {
        Part selectedPart = partsTable.getSelectionModel().getSelectedItem();
        if(selectedPart == null)
            return;

        {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this part?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                deletePart(selectedPart);
            }
        }
    }

    /**
     * The onDeleteProductButton opens a confirmation alert that if selected will delete the selected Product
     * from Inventory. However, if the selected product has associated parts an error will be returned and the
     * user will have to remove the parts before deleting the product.
     * @param event
     */

    @FXML
    void onDeleteProductButton(ActionEvent event) {
        Product selectedProduct = productsTable.getSelectionModel().getSelectedItem();
        if(selectedProduct == null)
            return;
        if(!selectedProduct.getAllAssociatedParts().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Cannot delete product with associated parts.");
            alert.showAndWait();
        }

        else{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this part?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                deleteProduct(selectedProduct);
            }
        }
    }

    /**
     * The onExitButton gives a confirmation alert that if selected will end the program.
     * @param event
     */

    @FXML
    void onExitButton(ActionEvent event) {
        {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are sure you want to exit?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                System.exit(0);
            }
        }
    }

    /**
     * The onModifyPartButton opens the ModifyPart.fxml page and sends the data for the selected part to that page.
     * @param event
     * @throws IOException
     */

    @FXML
    void onModifyPartButton(ActionEvent event) throws IOException {
        if (partsTable.getSelectionModel().getSelectedItem() == null)
            return;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("ModifyPart.fxml"));
        loader.load();
        ModifyPartController modifyController = loader.getController();
        modifyController.sendPart(partsTable.getSelectionModel().getSelectedItem());

        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * The onModifyProductButton opens the ModifyProduct.fxml page and send the data for the selected product
     * to that page.
     * @param event
     * @throws IOException
     */

    @FXML
    void onModifyProductButton(ActionEvent event) throws IOException {
        if (productsTable.getSelectionModel().getSelectedItem() == null)
            return;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("ModifyProduct.fxml"));
        loader.load();
        ModifyProductController modifyController = loader.getController();
        modifyController.sendParts(productsTable.getSelectionModel().getSelectedItem());
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * The onPartsSearch takes the user entered data in that search bar and returns any parts whose name or ID matches.
     * @param event
     */

    @FXML
    void onPartsSearch(ActionEvent event) {
        String q = mainSearchPartText.getText();
        ObservableList<Part> searchPart = Inventory.lookupPart(q);
        if (searchPart.size() == 0) {
            try {
                int partId = Integer.parseInt(q);
                Part p = Inventory.lookupPart(partId);
                if (p != null)
                    searchPart.add(p);
            }
            catch (NumberFormatException e){

            }
        }
        if (searchPart.size() == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialogue");
            alert.setContentText("No matching parts.");
            alert.showAndWait();
        }
        partsTable.setItems(searchPart);
    }

    /**
     * The onProductSearch takes the user entered string in the search bar and returns any products in Inventory
     * that have matching names or IDs.
     * @param event
     */

    @FXML
    void onProductsSearch(ActionEvent event) {
        String q = mainSearchProductText.getText();
        ObservableList<Product> searchProduct = Inventory.lookupProduct(q);
        if (searchProduct.size() == 0) {
            try {
                int productId = Integer.parseInt(q);
                Product p = Inventory.lookupProduct(productId);
                if (p != null)
                    searchProduct.add(p);
            }
            catch (NumberFormatException e){

            }
        }
        if (searchProduct.size() == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialogue");
            alert.setContentText("No matching products.");
            alert.showAndWait();
        }
        productsTable.setItems(searchProduct);
    }

}


