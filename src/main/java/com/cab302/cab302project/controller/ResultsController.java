package com.cab302.cab302project.controller;

import com.cab302.cab302project.model.session.Session;
import javafx.fxml.FXML;
import javafx.scene.text.Text;


public class ResultsController{

    @FXML
    private Text resultsData;

    public void setResults(int correct, int incorrect, Session session) {
        StringBuilder sb =  new StringBuilder();
        sb.append("You got ");
        sb.append(correct);
        sb.append(" correct ");
        sb.append("and ");
        sb.append(incorrect);
        sb.append(" incorrect ");
        resultsData.setText(sb.toString());
    }
}
