module uk.ac.aston.jpd.simulation{
	requires javafx.fxml;
	requires javafx.base;
	requires javafx.controls;
	requires javafx.graphics;
	
	requires junit;
	
	opens uk.ac.aston.jpd.simulation;
	opens uk.ac.aston.jpd.simulation.model;
	opens uk.ac.aston.jpd.simulation.model.entities;
	opens uk.ac.aston.jpd.simulation.model.entities.users;
	opens uk.ac.aston.jpd.simulation.visuals;
}