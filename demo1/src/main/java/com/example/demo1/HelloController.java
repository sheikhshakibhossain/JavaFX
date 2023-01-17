package com.example.demo1;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.io.*;
import java.util.*;

public class HelloController {
    @FXML
    protected Label cgpaLabel;
    @FXML
    protected TextField idTextField;

    @FXML
    protected Label errorLabel;

    @FXML
    protected void showCGPA() {
        if (idTextField.getText() == "") {
            errorLabel.setText("");
            cgpaLabel.setText("CGPA: ");
            return;
        }
        String id = idTextField.getText() + ".csv";
        getCGPA(id);
    }
    protected void getCGPA(String fileName) {
        String dataPath = System.getProperty("user.home");
        if (System.getProperty("os.name").equals("Linux")) {
            dataPath = dataPath + "/Documents";
        } else {
            dataPath = dataPath + "\\Documents";
        }

        File file = new File(dataPath, fileName);
        double cgpa = 0, previousCredit=0;
        try {
            Scanner fileScanner = new Scanner(file);
            int i=0;
            while (fileScanner.hasNext()) {
                String[] str = fileScanner.nextLine().split(",");;
                if (i == 0) {
                    i++;
                    continue;
                }
                try {
                    if (str[2].equals("0") || str[2].equals("0.0") || str[2].equals("0.00")) {
                        continue; // grade is null
                    }
                    cgpa = ( (previousCredit*cgpa) + (Double.parseDouble(str[1])*Double.parseDouble(str[2])) ) / (previousCredit+Double.parseDouble(str[1]));
                    previousCredit += Double.parseDouble(str[1]);
                } catch (Exception e) {} // fixed ArrayIndexOutOfBoundsException
            }
            fileScanner.close();
            cgpaLabel.setText( "CGPA: " + (Math.round(cgpa*100.0)/100.0 ) );
            errorLabel.setStyle("-fx-text-fill: green; -fx-font-size: 17px;");
            errorLabel.setText("Success!"); // reset errorLabel
        } catch (FileNotFoundException e) {
            cgpaLabel.setText("CGPA: ");
            errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 17px;");
            errorLabel.setText("Error! " + fileName +" was not found in directory!");
        } catch (ArrayIndexOutOfBoundsException e) {
            cgpaLabel.setText("CGPA: ");
            errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 17px;");
            errorLabel.setText("Error! Please fix the CSV file");
        } catch (Exception e) {
            cgpaLabel.setText("CGPA: ");
            errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 17px;");
            errorLabel.setText("Error! Something went wrong");
        }
    }
}