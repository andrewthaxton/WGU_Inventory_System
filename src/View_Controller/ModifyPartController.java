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
 * The ModifyPartController class inherits from Inventory and is the controller for the ModifyPart.fxml page.
 */

public class ModifyPartController extends Inventory implements Initializable {

    @FXML
    private Label modifyPartLabel;

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
     * The partID is the int where the ID of the part being modified is saved.
     */

    public int partID;

    /**
     * The wasInHouse is the boolean value whether the part was previously in-house or not.
     */

    boolean wasInHouse;

    /**
     * The onInHouseRadio sets the label to prompt for a Machine ID.
     * @param event
     */

    @FXML
    void onInHouseRadio(ActionEvent event) {
        machCompLabel.setText("Machine ID");
    }

    /**
     * The onOutsourcedRadio sets the label to prompt for a Company Name.
     * @param event
     */

    @FXML
    void onOutsourcedRadio(ActionEvent event) {
        machCompLabel.setText("Company Name");
    }

    /**
     * The onPartCancelButton gives a confirmation alert that if selected will exit the current page and open the
     * MainScreen.fxml page.
     * @param event
     * @throws IOException
     */

    @FXML
    void onPartCancelButton(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to cancel and lose all changes?");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {
            setStage(event, "MainScreen.fxml");
        }
    }

    /**
     * The onPartSaveButton runs the checkNewPart method and determines if the modified part meets all parameters.
     * If it does not then an error specifying the issue is given.
     * If it does then the modified part is saved.
     * @param event
     * @throws IOException
     */

    @FXML
    void onPartSaveButton(ActionEvent event) throws IOException{
        try {
            checkNewPart(lookupPart(partID));
            if((lookupPart(partID).getMax() < lookupPart(partID).getMin())
            || (lookupPart(partID).getStock() < lookupPart(partID).getMin())
            || (lookupPart(partID).getStock() > lookupPart(partID).getMax())){
                throw new stockErrorException();
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
     * The sendPart method takes the data of the selected part and displays it in the appropriate fields.
     * @param part
     */

    public void sendPart(Part part) {
        partIDTextbox.setText(String.valueOf(part.getId()));
        partNameTextbox.setText(part.getName());
        partInvTextbox.setText(String.valueOf(part.getStock()));
        partMinTextbox.setText(String.valueOf(part.getMin()));
        partMaxTextbox.setText(String.valueOf(part.getMax()));
        partPriceCostTextbox.setText(String.valueOf(part.getPrice()));
        partID = part.getId();

        if (part instanceof InhousePart) {
            inHouseRadio.fire();
            machCompLabel.setText("Machine ID");
            wasInHouse = true;
            machCompTextbox.setText(String.valueOf(((InhousePart)part).getMachineId()));
        }
        if (part instanceof OutsourcedPart) {
            outsourcedRadio.fire();
            machCompLabel.setText("Company Name");
            wasInHouse = false;
            machCompTextbox.setText(((OutsourcedPart)part).getCompanyName());
        }
    }

    /**
     * This initialize method was left blank and was not needed.
     * @param url
     * @param resourceBundle
     */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    /**
     *The checkNewPart method checks whether the part has made a switch between being in-house or outsourced.
     * If it is the same then the updateData method is called.
     * If it is different then the old part is deleted and a new one is created in its place.
     * @param p
     * @throws IOException
     */

    public void checkNewPart(Part p) throws IOException{
            boolean nowInHouse = (inHouseRadio.isSelected());
            int id = Integer.parseInt(partIDTextbox.getText());
            String name = partNameTextbox.getText();
            double price = Double.parseDouble(partPriceCostTextbox.getText());
            int stock = Integer.parseInt(partInvTextbox.getText());
            int min = Integer.parseInt(partMinTextbox.getText());
            int max = Integer.parseInt(partMaxTextbox.getText());

            if (wasInHouse && nowInHouse) {
                updateData(lookupPart(partID));
            }
            if (wasInHouse && !nowInHouse) {
                String compName = machCompTextbox.getText();
                deletePart(lookupPart(partID));
                OutsourcedPart replacement = new OutsourcedPart(id, name, price, stock, min, max, compName);
                Inventory.addPart(replacement);
            }
            if (!wasInHouse && nowInHouse) {
                int machineID = Integer.parseInt(machCompTextbox.getText());
                deletePart(lookupPart(partID));
                InhousePart replacement = new InhousePart(id, name, price, stock, min, max, machineID);
                Inventory.addPart(replacement);
            }
            if (!wasInHouse && !nowInHouse) {
                updateData(lookupPart(partID));
            }
    }

    /**
     * The updateData method sets the part to have the newly entered data.
     * RUNTIME ERROR - I was using the partPriceCostLabel instead of the partPriceCostTextbox to set the price of p.
     * This resulted in my NumberFormatException being throws because I was trying to pass a string as double.
     * I corrected my typo so that I was retrieving the text from the textbox instead of the label.
     * @param p
     */

    public void updateData(Part p) {
        p.setName(partNameTextbox.getText());
        p.setStock(Integer.parseInt(partInvTextbox.getText()));
        p.setMin(Integer.parseInt(partMinTextbox.getText()));
        p.setMax(Integer.parseInt(partMaxTextbox.getText()));
        p.setPrice(Double.parseDouble(partPriceCostTextbox.getText()));
        if (p instanceof InhousePart) {
            ((InhousePart)p).setMachineId(Integer.parseInt(machCompTextbox.getText()));
        }
        if (p instanceof OutsourcedPart) {
            ((OutsourcedPart)p).setCompanyName(machCompTextbox.getText());
        }
    }
}
