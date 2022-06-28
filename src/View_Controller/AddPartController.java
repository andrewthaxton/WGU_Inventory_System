package View_Controller;

import Model.InhousePart;
import Model.Inventory;
import Model.OutsourcedPart;
import Model.Part;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * The AddPartController class inherits from the Inventory class and is the controller for the AddPart.fxml.
 */

public class AddPartController extends Inventory implements Initializable {

    @FXML
    private Label addPartLabel;

    @FXML
    private RadioButton inHouseRadio;

    @FXML
    private ToggleGroup outsourcedToggle;

    @FXML
    private RadioButton outsourcedRadio;

    @FXML
    private Label partIDLabel;

    @FXML
    private TextField partIDTextbox;

    @FXML
    private Label partNameLabel;

    @FXML
    private Label partInvLabel;

    @FXML
    private Label partPriceCostLabel;

    @FXML
    private Label partMaxLabel;

    @FXML
    private Label machCompLabel;

    @FXML
    private TextField partNameTextbox;

    @FXML
    private TextField partInvTextbox;

    @FXML
    private TextField partPriceCostTextbox;

    @FXML
    private TextField partMaxTextbox;

    @FXML
    private TextField machCompTextbox;

    @FXML
    private Label partMinLabel;

    @FXML
    private TextField partMinTextbox;

    @FXML
    private Button partSaveButton;

    @FXML
    private Button partCancelButton;

    /**
     * The onInHouseRadio changes the label to prompt for a Machine ID.
     * @param event
     */

    @FXML
    void onInHouseRadio(ActionEvent event) {
        machCompLabel.setText("Machine ID");
    }

    /**
     * The onOutsourcedRadio changes the label to prompt for a Company Name.
     * @param event
     */

    @FXML
    void onOutsourcedRadio(ActionEvent event) {
        machCompLabel.setText("Company Name");
    }

    /**
     * The onPartCancelButton closes the AddPart.fxml and opens the MainScreen.fxml.
     * @param event
     * @throws IOException
     */

    @FXML
    void onPartCancelButton(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This will clear all text field values, do you want to continue?");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            setStage(event, "MainScreen.fxml");
        }

    }

    /**
     * The onPartSaveButton saves and adds a new part to the Inventory if it meets all parameters.
     * If the part has invalid data then this class gives an error.
     * @param event
     * @throws IOException
     */

    @FXML
    void onPartSaveButton(ActionEvent event) throws IOException {
        try{
            int id = Integer.parseInt(partIDTextbox.getText());
            String name = partNameTextbox.getText();
            int stock = Integer.parseInt(partInvTextbox.getText());
            double price = Double.parseDouble(partPriceCostTextbox.getText());
            int min = Integer.parseInt(partMinTextbox.getText());
            int max = Integer.parseInt(partMaxTextbox.getText());
            boolean inHouse;
            inHouse = (inHouseRadio.isSelected());

            if (max < min || stock < min || stock > max){
                throw new stockErrorException();
            }

            if(inHouse){
                int machineId = Integer.parseInt(machCompTextbox.getText());
                Inventory.addPart(new InhousePart(id, name, price, stock, min, max, machineId));
            }
            else{
                String companyName = machCompTextbox.getText();
                Inventory.addPart(new OutsourcedPart(id, name, price, stock, min, max, companyName));
            }
            setStage(event, "MainScreen.fxml");
        }catch(NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialogue");
            alert.setContentText("Please enter a valid value for each text field");
            alert.showAndWait();
        }catch(stockErrorException s){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialogue");
            alert.setContentText("Max must be greater than Stock which must be greater than Min. Min < Stock < Max");
            alert.showAndWait();
        }
    }

    /**
     * The setIdField automatically generates and sets the ID when the AddProduct.fxml page is loaded.
     */

    public void setIdField() {
        int id = Inventory.getAllParts().size() + 1;
        for(Part p: Inventory.getAllParts()){
            if(p.getId() == id){
                do{
                    id++;
                }while(p.getId() == id);
            }
        }
        partIDTextbox.setText(String.valueOf(id));
    }

    /**
     * This initializes the page as an In-House Product.
     * @param url
     * @param resourceBundle
     */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        inHouseRadio.fire();
    }
}
