package tcsmp.application;

import java.io.IOException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import tcsmp.client.Client;

public class ClientMain extends Application{

	public static Client current_user; 

	public static void redirect(String file,ActionEvent event) {
		try {
			ClientMain clientMain = new ClientMain();
			clientMain.redirectHelper(file,event);
		}
		catch(Exception e) {
			e.printStackTrace();
		}

	}
	
	@Override
	public void start(Stage primaryStage) {
		try {
			AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("Sample.fxml"));
			Scene scene = new Scene(root,600,600);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.setTitle("TCSMP Project");
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
	
	private void redirectHelper(String file, ActionEvent event) throws IOException {
		Parent root = (AnchorPane)FXMLLoader.load(getClass().getResource(file));
		Scene scene = new Scene(root);
		Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		stage.setScene(scene);
		stage.show();

	}
}
