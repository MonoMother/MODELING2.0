package com.example.strashno;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class Calculator implements Initializable {
    // Пустой конструктор, потому что без него НИЧЕГО НЕ РАБОТАЕТ
    public Calculator() {}
    @FXML
    public TextField outputTF;
    private Controller controller;

    public void setController(Controller controller) {
        this.controller = controller;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {}

    @FXML
    private void onButtonClick(ActionEvent event) {
        if (event.getTarget() instanceof Button btn) {
            outputTF.setText(outputTF.getText().trim() + btn.getText().trim());
        }
    }

    @FXML
    private void onDELClick(ActionEvent event) {
        if (outputTF.getText().length() > 0) {
            outputTF.setText(outputTF.getText().substring(0, outputTF.getText().length() - 1));
        }
    }
    @FXML
    private void onDELALLClick(ActionEvent event) {
        outputTF.setText("");
    }
    @FXML
    private void onOKClick(ActionEvent event) {
        controller.setText(outputTF.getText()); // Используем ссылку на Controller для установки текста
        controller.setTextFirstTrain(outputTF.getText());
        ((Stage)(((Button)event.getTarget()).getScene().getWindow())).close();
    }
}