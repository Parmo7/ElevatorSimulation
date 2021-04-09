package uk.ac.aston.jpd.simulation;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import uk.ac.aston.jpd.simulation.visuals.GUIConfigController;


import java.io.IOException;

/**
 * {@code GUILauncher} represents a simulation with a graphical user interface.
 * It allows to set a number of parameters before running the simulation itself.
 * 
 * @author Jims
 */
public class GUILauncher extends Application {

    public static void main(String[] args) {
        launch(args);
    }

	/**
	 * Entry point for the interactive graphical interface: loads the
	 * configuration's layout, creates and sets the controller class of it.
	 * 
	 * @param primaryStage window for the simulation configuration.
	 */
    @Override
    public void start(Stage primaryStage) throws IOException {
        final FXMLLoader loader = new FXMLLoader(); //creates this object so the FXML file can be loaded
        loader.setLocation(getClass().getResource("visuals/Configuration.fxml")); //set the location of where the fxml is
        GUIConfigController configController = new GUIConfigController(); //create object for the controller for the config
        loader.setController(configController); //set the controller
        final Parent root = loader.load(); //store the ui layout

        //sets the stage for the application
        final Scene configScene = new Scene(root, 400, 380);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Simulation Configuration");
        primaryStage.setScene(configScene);
        primaryStage.show();
    }
}
