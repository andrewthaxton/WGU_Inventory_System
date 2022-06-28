package View_Controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Javadocs located: Andrew_Thaxton_Inventory_System\src\Javadocs
 * This is the Main class that runs the application.
 * RUNTIME ERROR: This is located in the ModifyPartController on the UpdateData member.
 * FUTURE ENHANCEMENT: If I were to make an enhancement to this program it would be to implement a
 * Database Management System. This could be achieved by implementing a JDBC class that would allow
 * the application to interact with the DBMS.
 */

public class Main extends Application {

    /**
     * This opens the MainScreen.fxml page.
     * @param primaryStage
     * @throws Exception
     */

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
        primaryStage.setTitle("Thaxton Inventory System");
        primaryStage.setScene(new Scene(root, 800, 500));
        primaryStage.show();
    }


    /**
     * This launches the program.
     * @param args
     */

    public static void main(String[] args) { launch(args); }
}
