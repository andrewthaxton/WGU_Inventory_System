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
 * The ModifyProductController class inherits from Inventory and is the controller for the ModifyProduct.fxml screen.
 */

public class ModifyProductController extends Inventory implements Initializable {

    @FXML
    private Label modifyProductLabel;

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
    private int productID;


    /**
     * The onAddPartButton adds the selected part to associated parts list for the product.
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
     * The onProductCancelButton gives a confirmation alert that if selected will close the current screen and open
     * the MainScreen.fxml page.
     * @param event
     * @throws IOException
     */

    @FXML
    void onProductCancelButton(ActionEvent event) throws IOException{
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you would like to cancel your changes?");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            setStage(event, "MainScreen.fxml");
        }
    }

    /**
     * The onProductSaveButton checks if the modified product meets the parameters.
     * If it does not then an error is given stating what parameter is not being met.
     * If it does then the product is updated and the MainScreen.fxml page is opened.
     * @param event
     * @throws IOException
     */

    @FXML
    void onProductSaveButton(ActionEvent event) throws IOException {
        try {
            updateProduct(lookupProduct(productID));
            if ((lookupProduct(productID).getMax() < lookupProduct(productID).getMin())
                    || (lookupProduct(productID).getStock() < lookupProduct(productID).getMin())
                    || (lookupProduct(productID).getStock() > lookupProduct(productID).getMax())) {
                throw new stockErrorException();
            }
            setStage(event, "MainScreen.fxml");
        }catch (NumberFormatException e) {
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
     * The onRemovePartButton gives a confirmation alert that if selected then the selected part will be removed
     * from the product.
     * @param event
     */

    @FXML
    void onRemovePartButton(ActionEvent event) {
        Part selectedPart = selectedPartTable.getSelectionModel().getSelectedItem();

        if(selectedPart == null)
            return;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to remove this part?");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {
            selectedParts.remove(selectedPart);
        }
    }

    /**
     * The initialize sets up the page to have two table. One table for a list of all parts and one for
     * associated parts.
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
     * The sendParts method takes the selected product's data and displays it in the appropriate fields.
     * @param p
     */

    public void sendParts(Product p) {
        productID = p.getId();
        productIDTextbox.setText(String.valueOf(p.getId()));
        productNameTextbox.setText(p.getName());
        productInvTextbox.setText(String.valueOf(p.getStock()));
        productMinTextbox.setText(String.valueOf(p.getMin()));
        productMaxTextbox.setText(String.valueOf(p.getMax()));
        productPriceTextbox.setText(String.valueOf(p.getPrice()));
        choosePartTable.setItems(Inventory.getAllParts());
        selectedParts.addAll(p.getAllAssociatedParts());
        selectedPartTable.setItems(selectedParts);
    }

    /**
     * The onProductPartSearch method takes the user entered string from the search bar and checks all parts for
     * matching names or IDs.
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

    /**
     * The updateProduct method sets the product's fields to the newly entered values.
     * @param p
     * @throws IOException
     */

    public void updateProduct(Product p) throws IOException {
            String name = productNameTextbox.getText();
            int stock = Integer.parseInt(productInvTextbox.getText());
            int min = Integer.parseInt(productMinTextbox.getText());
            int max = Integer.parseInt(productMaxTextbox.getText());
            double price = Double.parseDouble(productPriceTextbox.getText());

            p.setName(name);
            p.setStock(stock);
            p.setMin(min);
            p.setMax(max);
            p.setPrice(price);
            p.getAllAssociatedParts().clear();
            for (Part part : selectedParts) {
                p.addAssociatedPart(part);
            }
    }

}
