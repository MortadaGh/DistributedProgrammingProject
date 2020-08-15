package tcsmp.controller;

import tcsmp.application.ClientMain;
import tcsmp.utils.DataUtils;
import tcsmp.utils.Email;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;


public class ComposeController implements Initializable{
	
	public static Email e;
	public static boolean clientFound;
	@FXML
    private TextField to;

    @FXML
    private TextField subject;

    @FXML
    private TextArea body;
    
    
    @Override
	public void initialize(URL location, ResourceBundle resources) {
    	if(e != null) {
    		to.setText(e.getTo());
    		subject.setText(e.getSubject());
    		body.setText(e.getContent());
    	} else {
    		to.setText("");
    		subject.setText("");
    		body.setText("");
    	}
	}
    
    
    @FXML
    void goToInbox(ActionEvent event) {
    	ClientMain.redirect("Inbox.fxml", event);
    }

    @FXML
    void send(ActionEvent event) {
    	clientFound = true;
    	try {
    		if(!to.getText().isEmpty() && !subject.getText().isEmpty() && !body.getText().isEmpty()) {
    			if (!to.getText().matches(DataUtils.EMAIL_PATTERN)) {
            		AlertController.error("Invalid email pattern for the destination email");
            		return;
        		} else {
        			
        				PuzzleController.e = new Email(ClientMain.current_user.getEmail(),to.getText(), subject.getText(), body.getText());
            			ClientMain.current_user.getOut().writeUTF("Message:" + to.getText() + ":" + subject.getText() + ":" + body.getText());
            			try {
            			TimeUnit.SECONDS.sleep(1);
            			if(clientFound) {
            				TimeUnit.SECONDS.sleep(1);
            				ClientMain.redirect("Puzzle.fxml", event);
            			}
            			else {
            				AlertController.error("Client with email '" + to.getText() + "' does not exist");
            			}
        			} catch (InterruptedException e) {
        				e.printStackTrace();
        			}	
        		}
    		} else {
        		AlertController.error("All fields are required");
        		return;
    		}
	
		} catch (IOException e) {
			AlertController.error("Server Error: Error while sending email to the server");
			e.printStackTrace();
		}
    }

}
