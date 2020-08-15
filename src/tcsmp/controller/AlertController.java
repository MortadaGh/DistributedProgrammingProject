package tcsmp.controller;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class AlertController {
	
	public static void information(String text) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setContentText(text);
		alert.setHeaderText("");
		alert.showAndWait();
	}
	
	
	public static void error(String text) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setContentText(text);
		alert.setHeaderText("");
		alert.showAndWait();
	}
	
	public static void confirmation(String text) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setContentText(text);
		alert.setHeaderText("");
		alert.showAndWait();
	}

}
