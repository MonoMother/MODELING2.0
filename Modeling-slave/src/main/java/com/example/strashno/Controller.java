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
    private int currentStep;
    int row;
    Stack<Character> tempStack = new Stack<>();
    private Stack<Character> stack = new Stack<>();
    private Stack<Character> greatStack = new Stack<>();
    private StringBuilder postfix = new StringBuilder();
    private int currentIndex = 0;
    private boolean isConversionComplete;
    //в таблице в [8,8] исправила 7 на 2, потому что это не ошибка, такая ситуация корректна и может возникнуть в ходе
    //работы программы, пример: A+(B-C*sin(A))
    //в ходе выполнения программы в стеке остаётся sin, потому что мы удаляем оттуда открывающую и закрывающую скобку
    //а потом на вход поступает ')'
    char[][] table = {
            {' ', '|', '+', '-', '*', '/', '^', '(', ')', 'F', 'P'},
            {'|', '4', '1', '1', '1', '1', '1', '1', '5', '1', '6'},
            {'+', '2', '2', '2', '1', '1', '1', '1', '2', '1', '6'},
            {'-', '2', '2', '2', '1', '1', '1', '1', '2', '1', '6'},
            {'*', '2', '2', '2', '2', '2', '1', '1', '2', '1', '6'},
            {'/', '2', '2', '2', '2', '2', '1', '1', '2', '1', '6'},
            {'^', '2', '2', '2', '2', '2', '2', '1', '2', '1', '6'},
            {'(', '5', '1', '1', '1', '1', '1', '1', '3', '1', '6'},
            {'F', '2', '2', '2', '2', '2', '2', '1', '2', '7', '6'},
    };
    @FXML
    private Text flag;
    @FXML
    private GridPane stackGridPane;
    @FXML
    private Button button;
    @FXML
    private Button bigButton;
    @FXML
    private TextField inputText;
    @FXML
    private TextField outputText;
    @FXML
    private GridPane Deikstra;
    @FXML
    private GridPane firstTrain;
    @FXML
    private GridPane secondTrain;

    @FXML
    public void initialize() {
        createTable();
    }

    @FXML
    private void onButtonClick(ActionEvent event) {
        secondTrain.getChildren().removeIf(node -> node instanceof Text); //удаляем текст
        postfix.setLength(0);
        stack.clear();
        outputText.setText(convert(inputText.getText()));
        isConversionComplete = true;
        while (!greatStack.isEmpty()) {
            tempStack.push(greatStack.pop());
        }
    }

    @FXML
    private void onBigButtonClick(ActionEvent event) {
        secondTrain.getChildren().removeIf(node -> node instanceof Text); // Удаляем текст
        if (!isConversionComplete) {
            setTextSecondTrain(convertNextStep(inputText.getText()));

            if (!tempStack.isEmpty()) {
                char currentChar = tempStack.pop(); // Получаем верхний элемент без удаления
                Text letter = new Text(String.valueOf(currentChar));
                stackGridPane.add(letter, 0, row); // Добавляем символ в новую строку
                --row;
                GridPane.setHalignment(letter, HPos.CENTER);
                GridPane.setValignment(letter, VPos.CENTER);
            }

            System.out.println("Текущий шаг: " + currentStep); // Обновляем информацию о текущем шаге
        }

        if (isConversionComplete) {
            postfix.setLength(0);
            stack.clear();
            currentIndex = 0;
            isConversionComplete = false;
            setTextSecondTrain(convertNextStep(inputText.getText()));
            row = stackGridPane.getRowCount() - 1; // Получаем текущий индекс строки
            stackGridPane.getChildren().removeIf(node -> node instanceof Text); // Удаляем текст

            System.out.println("Текущий шаг: " + currentStep); // Обновляем информацию о текущем шаге
        }
    }


    public void setTextSecondTrain(String text) {
        for (int i = 0; i < text.length(); i++) {
            Text letter = new Text(String.valueOf(text.charAt(i)));
            secondTrain.add(letter, i, 0); // Добавляем символ в i-тую колонку
            GridPane.setHalignment(letter, HPos.CENTER); // Горизонтальное выравнивание
            GridPane.setValignment(letter, VPos.CENTER); // Вертикальное выравнивание
        }
    }
    public void setTextStackGridPane(Stack<Character> stack) {
        int c = stackGridPane.getRowCount()-1;
        for (int i = stack.size(); i >0; i--) {
            char currentChar = stack.get(stack.size()-i);
                Text letter = new Text(String.valueOf(currentChar));
                stackGridPane.add(letter, 0, c); // Добавляем символ в i-тую строку (вертикально)
                --c;
                GridPane.setHalignment(letter, HPos.CENTER); // Горизонтальное выравнивание
                GridPane.setValignment(letter, VPos.CENTER); // Вертикальное выравнивание

        }
        greatStack.clear();
    }
    public void handleCalculator(MouseEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(InFixToPostFix.class.getResource("Calculator.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Калькулятор");
        Parent root = fxmlLoader.load();
        Calculator calculatorController = fxmlLoader.getController();
        calculatorController.setController(this); // Устанавливаем связь с текущим Controller
        Scene scene = new Scene(root, 461, 426);
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

    private String convertNextStep(String infix) {
        if (currentIndex >= infix.length()) {
            isConversionComplete = true;
            return postfix.toString();
        }

        infix = infix.replace("sin", "s");
        infix = infix.replace("cos", "c");

        char c = infix.charAt(currentIndex);
        int row = findRow(stack);
        int col = findCol(c);

        if (row == -1 || col == -1) {
            throw new IllegalArgumentException("Invalid character: " + c);
        }

        int action = Character.getNumericValue(table[row][col]);

        switch (action) {
            case 1:
                stack.push(c);
                currentStep++;
                break;
            case 2:
                if (stack.peek() == 's') {
                    postfix.append(stack.pop());
                    postfix.append('i');
                    postfix.append('n');
                } else if (stack.peek() == 'c') {
                    postfix.append(stack.pop());
                    postfix.append('o');
                    postfix.append('s');
                } else {
                    postfix.append(stack.pop());
                }
                if (c != ')') {
                    stack.push(c);
                }
                currentStep++;
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
                currentStep++;
                break;
            case 4:
                return postfix.toString();
            case 5:
                throw new IllegalArgumentException("Mismatched parentheses");
            case 6:
                postfix.append(c);
                currentStep++;
                break;
            case 7:
                throw new IllegalArgumentException("Missing function argument");
        }

        currentIndex++;

        if (currentIndex >= infix.length()) {
            while (!stack.isEmpty()) {
                char popped = stack.pop();
                if (popped != '(') {
                    postfix.append(popped);
                }
                if (popped == 's') {
                    postfix.append('i');
                    postfix.append('n');
                }
                if (popped == 'c') {
                    postfix.append('o');
                    postfix.append('s');
                }
            }
            isConversionComplete = true;
        }
        return postfix.toString();
    }
    public String convert(String infix) {
        infix = infix.replace("sin", "s");
        infix = infix.replace("cos", "c");

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
                    greatStack.push(c);
                    break;
                case 2:
                    if (stack.peek() == 's'){
                        postfix.append(stack.pop());
                        postfix.append('i');
                        postfix.append('n');
                    } else if (stack.peek() == 'c'){
                        postfix.append(stack.pop());
                        postfix.append('o');
                        postfix.append('s');

                    } else postfix.append(stack.pop());
                    if (c!=')') {
                        stack.push(c);
                        greatStack.push(c);
                    }
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
            if (popped == 's'){
                postfix.append('i');
                postfix.append('n');
            }
            if (popped == 'c'){
                postfix.append('o');
                postfix.append('s');
            }
        }
        return postfix.toString();
    }

    private int findRow(Stack<Character> stack) {
        if (stack.isEmpty()) return 1;
        for (int i = 0; i < table.length; i++) {
            if (table[i][0] == stack.peek()) return i;
        }
        if ((stack.peek() == 's')||(stack.peek() == 'c')) return 8;
        return -1;
    }


    private int findCol(char c) {
        for (int j = 0; j < table[0].length; j++) {
            if (table[0][j] == c) return j;
            else if ((c == 'A')||(c == 'B')||(c == 'C')||(c == 'D')) return 10;
            else if ((c == 's') || (c == 'c')) return 9;
        }
        return -1;
    }
    }
