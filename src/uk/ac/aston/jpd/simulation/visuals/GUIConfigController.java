package uk.ac.aston.jpd.simulation.visuals;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The {@code GUIConfigController} class is the controller for the configuration window.
 * The simulation will be run according to the provided parameters.
 * 
 * @author Jims
 */
public class GUIConfigController {

	// stores the ID of the GUI objects
	@FXML
	private Slider sliderP;
	@FXML
	private Slider sliderQ;
	@FXML
	private Label labelP;
	@FXML
	private Label labelQ;

	// <Integer> is added, otherwise without it its .getValue() returns an object
	@FXML
	private Spinner<Integer> spinnerHours;
	@FXML
	private Spinner<Integer> spinnerSeed;
	@FXML
	private Spinner<Integer> spinnerFloors;
	@FXML
	private Spinner<Integer> spinnerElevatorCapacity;
	@FXML
	private Spinner<Integer> spinnerNonDeveloper;
	@FXML
	private Spinner<Integer> spinnerGoggle;
	@FXML
	private Spinner<Integer> spinnerMugtome;

	// used to store currentValue of probability this is for the slider
	@SuppressWarnings("unused")
	private int pValue;
	@SuppressWarnings("unused")
	private int qValue;

	/**
	 * {@code initialize()} updates the value of the label according to the slider's
	 * value.
	 */
	@FXML
	public void initialize() {
		// this updates the label text when the probabilities value are being changed
		sliderP.valueProperty().addListener((property, oldValue, newValue) -> {
			labelP.setText(String.valueOf(newValue));
			pValue = newValue.intValue();
		});
		sliderQ.valueProperty().addListener((property, oldValue, newValue) -> {
			labelQ.setText(String.valueOf(newValue));
			qValue = newValue.intValue();
		});
		pValue = (int) sliderP.getValue();
		qValue = (int) sliderQ.getValue();
	}

	/**
	 * {@code runPressed} takes the values the user has chosen from the
	 * configuration and pass it to the elevator GUI controller, while creating the
	 * window for the elevator simulation, loading the layout and setting its
	 * properties. Once that is done it will close the configuration window.
	 * 
	 * @throws IOException if the file for the elevator layout cannot be found.
	 */
	@FXML
	public void runPressed() {
		// stores the values required for the elevator simulation
		int hoursValue = spinnerHours.getValue();
		int seedValue = spinnerSeed.getValue();
		int numFloors = spinnerFloors.getValue();
		int elevatorCapacityValue = spinnerElevatorCapacity.getValue();
		int numNonDev = spinnerNonDeveloper.getValue();
		int numGoggles = spinnerGoggle.getValue();
		int numMugtome = spinnerMugtome.getValue();
		double pProbValue = sliderP.getValue();
		double qProbValue = sliderQ.getValue();

		// to properly adjust the window's size
		double sceneHeight = 100 * numFloors;
		if (numFloors < 7) {
			sceneHeight += 30;
		} else if (numFloors > 7) {
			sceneHeight -= 30;
		}

		try {
			final FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("ElevatorSimulationGUI.fxml"));
			// passes the values to the elevator's controller which is used to setup the
			// elevator simulation
			GUIElevatorController controller = new GUIElevatorController(hoursValue, seedValue, numFloors,
					elevatorCapacityValue, numNonDev, numGoggles, numMugtome, pProbValue, qProbValue);
			loader.setController(controller);

			final Parent root = loader.load();

			// sets the scene and stage and other properties of the stage for the elevator
			// simulation's GUI
			final Stage elevatorSimulationGUI = new Stage();
			final Scene elevatorScene = new Scene(root, 600, sceneHeight);
			elevatorSimulationGUI.setTitle("Elevator Simulation");
			elevatorSimulationGUI.setResizable(false);
			elevatorSimulationGUI.setScene(elevatorScene);
			elevatorSimulationGUI.show();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// when runPressed occurs it will close the configuration GUI and display the
		// elevator simulation GUI
		labelP.getScene().getWindow().hide();
	}

}