package uk.ac.aston.jpd.simulation.visuals;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import uk.ac.aston.jpd.simulation.model.Simulation;
import uk.ac.aston.jpd.simulation.model.entities.Building;
import uk.ac.aston.jpd.simulation.model.entities.Elevator;
import uk.ac.aston.jpd.simulation.model.entities.Floor;
import uk.ac.aston.jpd.simulation.model.entities.users.*;

import java.util.*;

/**
 * The class {@code GUIElevatorController} builds the {@link Simulation} and the
 * simulation's Graphical User Interface: specifically the user and floor
 * graphical objects and the elevator graphical container. At each tick, it also
 * updates the elevator's GUI according to the events that occur within the
 * {@link Simulation}
 * 
 * @author Jims
 */
public class GUIElevatorController {

	// stores the ID of the GUI objects
	@FXML private AnchorPane root;
	@FXML private VBox vBoxElevator;
	@FXML private Label labelTick;
	@FXML private Button buttonTickOne;
	@FXML private Button buttonTickTen;
	@FXML private Button buttonTickOneHundred;
	@FXML private VBox vBoxUserLabels;
	@FXML private HBox hBoxInQueue;

	final private int tick;
	final private int numFloors;
	final private Simulation simulation;
	private Building simulationBuilding;
	private Floor[] simulationFloors;
	private Elevator simulationElevator;
	private TextView tInterface;

	private VBox[] floorVBoxes;

	/**
	 * The constructor for the {@code GUIElevatorController} takes multiple
	 * parameters to create a {@link Simulation} object. <br>
	 * It then initialises {@link #simulationBuilding}, {@link #simulationFloor} and
	 * {@link #simulationElevator} based on the newly created simulation.
	 * 
	 * @param hours            how long the simulation will run for
	 * @param seed             value for the Random object to be used throughout the
	 *                         simulation
	 * @param numFloors        number of floors that will be displayed and created
	 * @param elevatorCapacity user capacity for the {@code Elevator}
	 * @param numNonDev        number of non developers in the simulation
	 * @param numGoggles       number of Goggle developers in the simulation
	 * @param numMugtome       number of Mugtome developers in the simulation
	 * @param pProb            the chosen probability P for the simulation
	 * @param qProb            the chosen probability Q for the simulation
	 */
	public GUIElevatorController(int hours, int seed, int numFloors, int elevatorCapacity, int numNonDev,
			int numGoggles, int numMugtome, double pProb, double qProb) {
		this.numFloors = numFloors;
		tick = hours * 360;
		floorVBoxes = new VBox[numFloors];

		simulation = new Simulation.Builder().seed(seed).probabilityP(pProb).probabilityQ(qProb).numFloors(numFloors)
				.elevatorCapacity(elevatorCapacity).nonDevelopers(numNonDev).goggles(numGoggles).mugtomes(numMugtome)
				.build();
		tInterface = new TextView();

		simulationBuilding = simulation.getBuilding();
		simulationFloors = simulationBuilding.getFloors();
		simulationElevator = simulationBuilding.getElevator();
	}

	/**
	 * Sets up the initial position of all graphical objects within the GUI. <br>
	 * Specifically, these objects are the floor entities, the elevator and the
	 * labels.
	 */
	@FXML
	private void initialize() {
		// Adjusts the height (Y) position of each object
		double yPosGraphicObjects = numFloors * 100 - 140;
		if (numFloors < 6) {
			yPosGraphicObjects += 40;
		} else if (numFloors > 7) {
			yPosGraphicObjects -= 30;
		}

		// this is used for the height position of the next floor object
		double yPos = yPosGraphicObjects + 80;

		// sets the layout of the existing objects from the GUI
		vBoxUserLabels.setLayoutY(yPos);
		buttonTickOne.setLayoutY(yPos);
		buttonTickTen.setLayoutY(yPos);
		buttonTickOneHundred.setLayoutY(yPos);
		labelTick.setLayoutY(yPos);
		hBoxInQueue.setLayoutY(yPos + 30);
		vBoxElevator.setLayoutY(yPosGraphicObjects);

		// creates a graphical object for each floor and sets its properties
		for (int i = 0; i < floorVBoxes.length; i++) {
			VBox floorVBox = new VBox();
			floorVBox.setId("floor" + simulationFloors[i].getLevel());
			floorVBox.setPrefHeight(75);
			floorVBox.setPrefWidth(400);
			floorVBox.setLayoutY(yPosGraphicObjects);
			floorVBox.setLayoutX(180);
			floorVBox.setStyle("-fx-background-color: #999999;");

			// creates 3 rows for each floor, which will be used to store the users 
			for (int numRow = 0; numRow < 3; numRow++) {
				HBox floorRowHBox = new HBox();
				floorRowHBox.setId("row" + numRow);
				floorRowHBox.setPrefHeight(25);
				floorRowHBox.setPrefWidth(100);
				floorRowHBox.setSpacing(5);
				floorRowHBox.setAlignment(Pos.CENTER);
				floorVBox.getChildren().add(floorRowHBox);
			}

			Label floorLabel = new Label();
			floorLabel.setLayoutY(yPosGraphicObjects - 19);
			floorLabel.setLayoutX(190);
			floorLabel.setStyle("-fx-font: 16 System;");
			floorLabel.setText("Floor " + simulationFloors[i].getLevel());

			floorVBoxes[i] = floorVBox;
			root.getChildren().add(floorVBox);
			root.getChildren().add(floorLabel);
			yPosGraphicObjects -= 90;
		}

	}

	/**
	 * The {@link #tickOnePressed()} event is triggered by the Elevator GUI. <br>
	 * It asks the simulation to tick and it then calls the {@link #tick()} method
	 * to update the GUI objects, if there have been any changes within the
	 * simulation.
	 */
	@FXML
	private void tickOnePressed() {
		// check if tick has reached the max tick
		if (checkTick()) {
			simulation.tick();
			// tick updates the graphical objects (such as elevator, users and floors) from
			// the elevator simulation GUI
			tick();
		}
	}

	/**
	 * The {@link #tickTenPressed()} event is triggered by the Elevator GUI. <br>
	 * It makes the simulation tick for 10 times and updates the interface accordingly.
	 */
	@FXML
	private void tickTenPressed() {
		for (int i = 0; i < 10; i++) {
			tickOnePressed();
		}
	}

	/**
	 * The {@link #tickOneHundredPressed()} event is triggered by the Elevator GUI. <br>
	 * It makes the simulation tick for 100 times and updates the interface accordingly.
	 */
	@FXML
	private void tickOneHundredPressed() {
		for (int i = 0; i < 100; i++) {
			tickOnePressed();
		}
	}

	/**
	 * It moves the graphical elevator according to the current floor. Specifically,
	 * it places the elevator at the same Y position (height) as the current floor.
	 */
	private void elevatorMoveY() {
		int floorLevel = simulationElevator.getCurrentFloor().getLevel();
		for (int i = 0; i < numFloors; i++) {
			vBoxElevator.setLayoutY(floorVBoxes[floorLevel].getLayoutY());
		}
	}

	/**
	 * Returns true or false depending on whether the current tick is less than the
	 * maximum tick.
	 * 
	 * @return true if the current tick is less than the maximum tick, false
	 *         otherwise;
	 */
	private boolean checkTick() {
		return simulation.getTick() < tick;
	}

	/**
	 * Updates the objects in the graphical interface based
	 * on the current status of the simulation.
	 */
	private void tick() {
		for (Floor f : simulationFloors) {

			List<User> userOnFloorAndQueue = new ArrayList<>();
			userOnFloorAndQueue.addAll(f.getInQueue());
			userOnFloorAndQueue.addAll(f.getOnFloor());

			clear(floorVBoxes[f.getLevel()]);

			for (User u : userOnFloorAndQueue) {
				createUserGraphics(floorVBoxes[f.getLevel()], u);
			}
		}

		List<User> eUsers = new ArrayList<>(simulationElevator.getUsers());

		clear(vBoxElevator);

		for (User u : eUsers) {
			createUserGraphics(vBoxElevator, u);
		}
		// update the text of the label according to the simulation's current tick
		labelTick.setText("Tick: " + Integer.toString(simulation.getTick()));

		elevatorMoveY();

		// asks the text interface to display the status of the simulation at the
		// current tick to show more details about users
		tInterface.visualize(simulation);
	}

	/**
	 * Creates a graphical object for the given {@code User}. <br>
	 * Sets a darker colour if the user is not in queue and a lighter one if he is
	 * is waiting in the queue.
	 * 
	 * @param vb   the graphical container of either the floor or elevator
	 * @param user the {@code User} to be displayed
	 */
	private void createUserGraphics(VBox vb, User user) {
		String userID = user.getID();

		Rectangle r = new Rectangle();

		r.setId(userID);
		r.setHeight(15);
		r.setWidth(15);
		r.setStroke(Color.BLACK);
		r.setStrokeType(StrokeType.INSIDE);

		// Users that are not in the queue are darker and those in the queue are lighter
		if (user.isInQueue()) {
			if (user instanceof Client) {
				r.setFill(Color.MEDIUMPURPLE);
			} else if (user instanceof MaintenanceCrew) {
				r.setFill(Color.LIGHTPINK);
			} else if (user instanceof NonDeveloper) {
				r.setFill(Color.LIGHTGREEN);
			} else if (user instanceof Mugtome) {
				r.setFill(Color.SALMON);
			} else if (user instanceof Goggle) {
				r.setFill(Color.LIGHTBLUE);
			}
		} else {
			if (user instanceof Client) {
				r.setFill(Color.DARKVIOLET);
			} else if (user instanceof MaintenanceCrew) {
				r.setFill(Color.DEEPPINK);
			} else if (user instanceof NonDeveloper) {
				r.setFill(Color.GREEN);
			} else if (user instanceof Mugtome) {
				r.setFill(Color.RED);
			} else if (user instanceof Goggle) {
				r.setFill(Color.BLUE);
			}
		}

		addUserInBox(vb, r);
	}

	/**
	 * Removes all the users within the given floor/elevator graphical object.
	 * 
	 * @param vb the floor/elevator container (VBox) that is to be cleared.
	 */
	private void clear(VBox vb) {
		// gets all the rows (HBox) within the floor/elevator
		ObservableList<Node> children = vb.getChildren();
		for (Node nHBox : children) {
			HBox hBox = (HBox) nHBox;
			// all users within the row (HBox) will be removed
			hBox.getChildren().clear();
		}
	}

	/**
	 * Adds the specified graphical object representing a user to the given
	 * container, either the floor or the elevator
	 * 
	 * @param vB   the floor/elevator container (VBox) to which a user graphical
	 *             object is to be added
	 * @param rect graphical object representing the user to be added and displayed
	 */
	private void addUserInBox(VBox vB, Rectangle rect) {
		// get all the rows (HBox) within the floor/elevator
		ObservableList<Node> floorChildren = vB.getChildren();
		for (Node nHBox : floorChildren) {
			if (nHBox instanceof HBox) {
				HBox hBox = (HBox) nHBox;
				int numNode = hBox.getChildren().size(); // used to limit how many user objects will be in a row
				if (numNode < 4 && vB.equals(vBoxElevator)) {
					hBox.getChildren().add(rect);
				} else if (numNode < 15 && !vB.equals(vBoxElevator)) {
					hBox.getChildren().add(rect);
				}
			}
		}
	}
}
