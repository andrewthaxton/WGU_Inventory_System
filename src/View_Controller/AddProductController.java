package View_Controller;

import Model.Inventory;
import Model.Part;
import Model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * The AddProductController inherits from the Inventory class and is the controller for AssProduct.fxml.
 */

public class AddProductController extends Inventory implements Initializable {

    @FXML
    private Label addProductLabel;

    @FXML
    private Label productIDLabel;

    @FXML
    private Label productNameLabel;

    @FXML
    private Label productInvLabel;

    @FXML
    private Label productPriceLabel;

    @FXML
    private Label productMaxLabel;

    @FXML
    private TextField productIDTextbox;

    @FXML
    private TextField productNameTextbox;

    @FXML
    private TextField productInvTextbox;

    @FXML
    private TextField productPriceTextbox;

    @FXML
    private TextField productMaxTextbox;

    @FXML
    private Label productMinLabel;

    @FXML
    private TextField productMinTextbox;

    @FXML
    private TableView<Part> choosePartTable;

    @FXML
    private TableColumn<?, ?> choosePartIdCol;

    @FXML
    private TableColumn<?, ?> choosePartNameCol;

    @FXML
    private TableColumn<?, ?> choosePartInventoryCol;

    @FXML
    private TableColumn<?, ?> choosePartPriceCol;

    @FXML
    private TextField productPartSearch;

    @FXML
    private Button productAddButton;

    @FXML
    private TableView<Part> selectedPartTable;

    @FXML
    private TableColumn<?, ?> selectedPartIdCol;

    @FXML
    private TableColumn<?, ?> selectedPartNameCol;

    @FXML
    private TableColumn<?, ?> selectedPartInventoryCol;

    @FXML
    private TableColumn<?, ?> selectedPartPriceCol;

    @FXML
    private Button removePartButton;

    @FXML
    private Button productSaveButton;

    @FXML
    private Button productCancelButton;

    private ObservableList<Part> selectedParts = FXCollections.observableArrayList();


    /**
     * The onAddPartButton adds the selected part from the table and adds it as an associated part.
     * @param event
     */

    @FXML
    void onAddPartButton(ActionEvent event) {
        Part selectedPart = choosePartTable.getSelectionModel().getSelectedItem();

        if(selectedPart == null)
            return;

        selectedParts.add(selectedPart);
    }

    /**
     * The onProductCancelButton gives a confirmation alert that if selected will close the current page
     * and open MainScreen.fxml.
     * @param event
     * @throws IOException
     */

    @FXML
    void onProductCancelButton(ActionEvent event) throws IOException{
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to cancel? This will clear all fields.");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            setStage(event, "MainScreen.fxml");
        }
    }

    /**
     * The onProductSaveButton attempts to create a new product if that product meets all of the parameters.
     * If the product does not then an error is given stating what needs to be fixed.
     * @param event
     * @throws IOException
     */

    @FXML
    void onProductSaveButton(ActionEvent event) throws IOException {
        try {
            int id = Integer.parseInt(productIDTextbox.getText());
            String name = productNameTextbox.getText();
            int stock = Integer.parseInt(productInvTextbox.getText());
            double price = Double.parseDouble(productPriceTextbox.getText());
            int min = Integer.parseInt(productMinTextbox.getText());
            int max = Integer.parseInt(productMaxTextbox.getText());

            Product newProduct = new Product(id, name, price, stock, min, max);
            for (Part p : selectedParts) {
                newProduct.addAssociatedPart(p);
            }

            if (max < min || stock < min || stock > max){
                throw new stockErrorException();
            }

            Inventory.addProduct(newProduct);
            setStage(event, "MainScreen.fxml");
        } catch (NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialogue");
            alert.setContentText("Invalid value(s)");
            alert.showAndWait();
        } catch (stockErrorException s) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialogue");
            alert.setContentText("Max must be greater than Stock which must be greater than Min. Min < Stock < Max");
            alert.showAndWait();
        }
    }

    /**
     * The onRemovePartButton triggers a confirmation alert that if  selected will remove am associated part from
     * the product.
     * @param event
     */

    @FXML
    void onRemovePartButton(ActionEvent event) {
        Part selectedPart = selectedPartTable.getSelectionModel().getSelectedItem();

        if(selectedPart == null)
            return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you would like to remove this part?");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK)
            selectedParts.remove(selectedPart);
    }

    /**
     * This initializes the AddProduct.fxml page with a table containing a list of all parts and a table for associated
     * parts.
     * @param url
     * @param resourceBundle
     */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        choosePartTable.setItems(Inventory.getAllParts());
        selectedPartTable.setItems(selectedParts);

        choosePartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        choosePartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        choosePartInventoryCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        choosePartPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        selectedPartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        selectedPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        selectedPartInventoryCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        selectedPartPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /**
     * The setIdField automatically generates and sets a new ID for the product you are creating.
     */

    public void setIdField() {
        int id = Inventory.getAllProducts().size() + 1;
        for(Product n: Inventory.getAllProducts()) {
            if(n.getId() == id) {
                do{
                    id++;
                }while(n.getId() == id);
            }
        }
        productIDTextbox.setText(String.valueOf(id));
    }

    /**
     * The onProductPartSearch takes what the user entered in the search bar and looks for the part containing the
     * string that was entered or the matching Part ID.
     * @param event
     */

    @FXML
    void onProductPartSearch(ActionEvent event) {
        String q = productPartSearch.getText();
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
        choosePartTable.setItems(searchPart);
    }


}
