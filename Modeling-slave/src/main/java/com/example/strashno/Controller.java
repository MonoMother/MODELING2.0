package com.example.strashno;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.geometry.HPos; //для горизонтального выравнивания
import javafx.geometry.VPos; //для вертикальноого выравнивания
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Stack;

public class Controller {

    char[][] table = {
            {' ', '|', '+', '-', '*', '/', '^', '(', ')', 'F', 'P'},
            {'|', '4', '1', '1', '1', '1', '1', '1', '5', '1', '6'},
            {'+', '2', '2', '2', '1', '1', '1', '1', '2', '1', '6'},
            {'-', '2', '2', '2', '1', '1', '1', '1', '2', '1', '6'},
            {'*', '2', '2', '2', '2', '2', '1', '1', '2', '1', '6'},
            {'/', '2', '2', '2', '2', '2', '1', '1', '2', '1', '6'},
            {'^', '2', '2', '2', '2', '2', '2', '1', '2', '1', '6'},
            {'(', '5', '1', '1', '1', '1', '1', '1', '3', '1', '6'},
            {'F', '2', '2', '2', '2', '2', '2', '1', '7', '7', '6'},
    };
    @FXML
    private Button button;
    @FXML
    private TextField inputText;
    @FXML
    private GridPane Deikstra;
    @FXML
    private GridPane firstTrain;

    @FXML
    public void initialize() {
        createTable();
    }

    @FXML
    private void onButtonClick(ActionEvent event) {
        System.out.println(convert(inputText.getText()));
    }

    public void handleCalculator(MouseEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(InFixToPostFix.class.getResource("Calculator.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Калькулятор");
        Parent root = fxmlLoader.load();
        Calculator calculatorController = fxmlLoader.getController();
        calculatorController.setController(this); // Устанавливаем связь с текущим Controller
        Scene scene = new Scene(root, 379, 423);
        stage.setScene(scene);
        stage.initModality(Modality.NONE);
        stage.show();
    }

    public void setText(String text) {
        inputText.setText(text);
    }

    public void setTextFirstTrain(String text) {
        firstTrain.getChildren().removeIf(node -> node instanceof Text); //удаляем текст
        for (int i = 0; i < text.length(); i++) {
            Text letter = new Text(String.valueOf(text.charAt(i)));
            firstTrain.add(letter, i, 0); // Добавляем символ в i-тую колонку
            GridPane.setHalignment(letter, HPos.CENTER); // Горизонтальное выравнивание
            GridPane.setValignment(letter, VPos.CENTER); // Вертикальное выравнивание
        }
    }

    public void createTable() {

        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[i].length; j++) {
                Label label = new Label(String.valueOf(table[i][j]));
                if ((i==0)||(j==0)){
                    label.setFont(Font.font("Courier New", FontWeight.BOLD, 16));
                } else label.setFont(Font.font("Courier New", 18));
                Deikstra.add(label, j, i);
                GridPane.setHalignment(label, HPos.CENTER); // Горизонтальное выравнивание
                GridPane.setValignment(label, VPos.CENTER); // Вертикальное выравнивание
            }
        }
    }
    public String convert(String infix) {
        Stack<Character> stack = new Stack<>();
        StringBuilder postfix = new StringBuilder();

        for (char c : infix.toCharArray()) {
            int row = findRow(stack);
            int col = findCol(c);

            if (row == -1 || col == -1) {
                throw new IllegalArgumentException("Invalid character: " + c);
            }

            int action = Character.getNumericValue(table[row][col]);

            switch (action) {
                case 1:
                    stack.push(c);
                    break;
                case 2:
                    postfix.append(stack.pop());
                    if (c!=')') stack.push(c);
                    break;
                case 3:
                    while (!stack.isEmpty()) {
                        char popped = stack.pop();
                        if (popped == '(') {
                            break;
                        } else {
                            postfix.append(popped);
                        }
                    }
                    break;
                case 4:
                    return postfix.toString();
                case 5:
                    throw new IllegalArgumentException("Mismatched parentheses");
                case 6:
                    postfix.append(c);
                    break;
                case 7:
                    throw new IllegalArgumentException("Missing function argument");
            }
        }

        while (!stack.isEmpty()) {
            char popped = stack.pop();
            if (popped != '(') {
                postfix.append(popped);
            }
        }

        return postfix.toString();
    }


    private int findRow(Stack<Character> stack) {
        if (stack.isEmpty()) {
            return 1;
        }
        for (int i = 0; i < table.length; i++) {
            if (table[i][0] == stack.peek()) {
                return i;
            }
        }
        return -1;
    }


    private int findCol(char c) {
        for (int j = 0; j < table[0].length; j++) {
            if (table[0][j] == c) {
                return j;
            }
            else if ((c == 'A')||(c == 'B')||(c == 'C')||(c == 'D')){
                return 10;
            }
        }
        return -1;
    }
    }
