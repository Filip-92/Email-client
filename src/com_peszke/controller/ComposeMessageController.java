package com_peszke.controller;

import com_peszke.EmailManager;
import com_peszke.controller.services.EmailSenderService;
import com_peszke.model.EmailAccount;
import com_peszke.view.ViewFactory;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.web.HTMLEditor;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

    public class ComposeMessageController extends BaseController implements Initializable {

        private List<File> attachments = new ArrayList<File>();

        @FXML
        private TextField recipientTextField;

        @FXML
        private TextField subjectTextField;

        @FXML
        private HTMLEditor htmlEditor;

        @FXML
        private Label errorLabel;

        @FXML
        private ChoiceBox<EmailAccount> emailAccountChoice;

        @FXML
        void attachButtonAction() {
            FileChooser fileChooser = new FileChooser();
            File selectedFile = fileChooser.showOpenDialog(null);
            if(selectedFile != null){
                attachments.add(selectedFile);
            }
        }

        @FXML
        void sendButtonAction() {
            EmailSenderService emailSenderService = new EmailSenderService(
                    emailAccountChoice.getValue(),
                    subjectTextField.getText(),
                    recipientTextField.getText(),
                    htmlEditor.getHtmlText(),
                    attachments
            );
            emailSenderService.start();
            emailSenderService.setOnSucceeded(e->{
                EmailSendingResult emailSendingResult = emailSenderService.getValue();
                switch (emailSendingResult) {
                    case SUCCESS:
                        Stage stage = (Stage) recipientTextField.getScene().getWindow();
                        viewFactory.closeStage(stage);
                        break;
                    case FAILED_BY_PROVIDER:
                        errorLabel.setText("Provider error!");
                        break;
                    case FAILED_BY_UNEXPECTED_ERROR:
                        errorLabel.setText("Unexpected error!");
                        break;
                }
            });
        }

        public ComposeMessageController(EmailManager emailManager, ViewFactory viewFactory, String fxmlName) {
            super(emailManager, viewFactory, fxmlName);
        }

        @Override
        public void initialize(URL location, ResourceBundle resources) {
            emailAccountChoice.setItems(emailManager.getEmailAccounts());
            emailAccountChoice.setValue(emailManager.getEmailAccounts().get(0));
        }
    }
