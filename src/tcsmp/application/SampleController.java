package tcsmp.application;

import static java.lang.System.exit;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import tcsmp.client.Client;
import tcsmp.client.ClientThread;
import tcsmp.controller.AlertController;
import tcsmp.utils.DataUtils;
import tcsmp.utils.Email;
import tcsmp.utils.HostPort;

public class SampleController {
	@FXML
    private TextField email;

    @FXML
    void login(ActionEvent event) {
    	String _emailInput = email.getText();
    	
    	if (!_emailInput.matches(DataUtils.EMAIL_PATTERN)) {
    		AlertController.error("Invalid email pattern");
		} else {
	    	String[] splittedEmail = _emailInput.split("@");
			String domainName = splittedEmail[1];
			
			if (!DataUtils.servers.containsKey(domainName)) {
				AlertController.error("Invalid domain name the server only accept (gmail, hotmail)");
			} else {
				HostPort hp = DataUtils.servers.get(domainName);
				ClientMain.current_user = new Client(_emailInput, hp);
				
		    	if(ClientMain.current_user == null) {
		    		return;
		    	} else {
		    		if(ClientMain.current_user.getSocket() == null) {
		    			return;
		    		}
		    		// Register to the server
		    		String regestrationRequest = "Register:" + _emailInput;		    		
		    		try {
			    		ClientMain.current_user.getOut().writeUTF(new String(regestrationRequest));
						ClientMain.current_user.setMessage_in(ClientMain.current_user.getIn().readUTF());
						if (!ClientMain.current_user.getMessage_in().startsWith("REGISTRATION OK")) {
							AlertController.information(ClientMain.current_user.getMessage_in());
							ClientMain.current_user.getSocket().close();
						} else {
							ClientThread cl = new ClientThread(ClientMain.current_user.getSocket(), ClientMain.current_user);
							cl.start();
							
							if(ClientMain.current_user != null) {
					    		System.out.println("Connected to ip: "+ ClientMain.current_user.getHost()+", Port: "+ClientMain.current_user.getPort());
//					    		DataUtils.HelloEmail.add(new Email(ClientMain.current_user.getEmail(),"Server@"+domainName, "Welcome Email", "Welcome "+ ClientMain.current_user.getEmail() + " to the TCSMP project"));
//					    		ClientMain.current_user.setEmails(DataUtils.HelloEmail);
					    		ClientMain.redirect("Inbox.fxml", event);
					    	}
						}
					} catch (IOException e) {
						AlertController.error("Error while sending the request to the server");
						e.printStackTrace();
					}
					
		    	}
		    	
			}
		
		}
    }
}
