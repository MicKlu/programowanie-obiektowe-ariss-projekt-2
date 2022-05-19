package com.example.projectnr2javapython;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class HelpSceneController {
    @FXML
    private Button ZamknijButton;
    @FXML
    private void onZamknijButtonClicked(){
        Stage stage = (Stage) ZamknijButton.getScene().getWindow();
        stage.close();
    }
}
