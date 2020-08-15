package tcsmp.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import tcsmp.application.ClientMain;
import tcsmp.utils.Email;

public class InboxController implements Initializable{
	@FXML
    private TableView<Email> tb;

    @FXML
    private TableColumn<Email, String> cl;

    @FXML
    private Text txt;
    
    @FXML
    private Label mail;


    @FXML
    private Text subject;
    
    @FXML
    void compose(ActionEvent event) {
    	ClientMain.redirect("Compose.fxml", event);
    }
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setInbox();
	}
	
	void setInbox() {
		mail.setText(ClientMain.current_user.getEmail());
		cl.setCellValueFactory(new PropertyValueFactory<Email, String>("from"));
		
		ObservableList<Email> msg = ClientMain.current_user.getEmailsForUI();
		tb.getItems().setAll(msg);
	}
	
	@FXML
    void changeSellection(MouseEvent event) {
		Email msg = tb.getSelectionModel().getSelectedItem();
		
		if(msg != null) {
			subject.setText("Subject: "+msg.getSubject());
			txt.setText(msg.getContent());
		}
    }
	
	@FXML
    void refresh(ActionEvent event) {
		try {
			ClientMain.current_user.getOut().writeUTF("Refresh");
			try {
				TimeUnit.SECONDS.sleep(1);
				setInbox();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e) {
			AlertController.error("Server Error: Error while getting user emails");
			e.printStackTrace();
		}
    }

}
