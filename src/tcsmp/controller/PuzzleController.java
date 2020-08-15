package tcsmp.controller;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import tcsmp.application.ClientMain;
import tcsmp.puzzle.Puzzle;
import tcsmp.utils.Email;


public class PuzzleController implements Initializable{
	
	public static Puzzle p;
	public static Email e;
	public static boolean correctPuzzel;
	
    @FXML
    private AnchorPane pane;
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setChars();
	}
	
	public void setChars() {
		ObservableList<Node> nodes = pane.getChildren();
		for(int i=0;i<36;i++) {
			Label l = (Label)nodes.get(i);
			l.setText(p.charAt(i)+"");
		}
	}
	
	
	
    @FXML
    void verify(ActionEvent event) {
    	try {
    		System.out.println("Client Solve: " + p.toString());
			ClientMain.current_user.getOut().writeUTF(p.toString());
			try {
				TimeUnit.SECONDS.sleep(1);
				if(correctPuzzel) {
		    		Alert alert = new Alert(AlertType.CONFIRMATION);
		    		alert.setTitle("Send Email");
			    	alert.setContentText("Email sent succesfully!");
			    	alert.setHeaderText("");
			    	Optional<ButtonType> result = alert.showAndWait();
			        if(result.get() == ButtonType.OK) {
						TimeUnit.SECONDS.sleep(1);
			        	ClientMain.redirect("Inbox.fxml", event);
			        }
		    	}
		    	else {
		    		Alert alert = new Alert(AlertType.ERROR);
		    		alert.setTitle("Error");
		            alert.setContentText("Email not sent!");
		            alert.showAndWait();
		            ComposeController.e = this.e;
					TimeUnit.SECONDS.sleep(1);
		        	ClientMain.redirect("Compose.fxml", event);
		    	}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			AlertController.error("Server Error: Error while verifying puzzel");
			e.printStackTrace();
		}
    	
    }
	
	@FXML
    void b0(ActionEvent event) {
		p.move(0);
		setChars();
    }

    @FXML
    void b1(ActionEvent event) {
    	p.move(1);
    	setChars();
    }

    @FXML
    void b2(ActionEvent event) {
    	p.move(2);
    	setChars();
    }

    @FXML
    void b3(ActionEvent event) {
    	p.move(3);
    	setChars();
    }

    @FXML
    void b4(ActionEvent event) {
    	p.move(4);
    	setChars();
    }

    @FXML
    void b5(ActionEvent event) {
    	p.move(5);
    	setChars();
    }

    @FXML
    void b6(ActionEvent event) {
    	p.move(6);
    	setChars();
    }

    @FXML
    void b7(ActionEvent event) {
    	p.move(7);
    	setChars();
    }

    @FXML
    void b8(ActionEvent event) {
    	p.move(8);
    	setChars();
    }
}
