package controllers;

import controllers.listeners.*;
import models.Model;
import views.View;

public class Controller {

    public Controller(Model model, View view) {

        view.registerButtonScores(new ButtonScores(model, view)); // Make a Leaderboard ActionListener for the button
        view.registerButtonNew(new ButtonNew(model, view)); // Make a New Game ActionListener for the button
        view.registerButtonSend(new ButtonSend(model, view)); // Make Send button to work
        view.registerButtonCancel(new ButtonCancel(model, view)); // Make a Cancel game ActionListener for the button
        view.registerComboBoxChange(new CategoryItemChange(model)); // Make a ComboBox ActionListener for the ComboBox
    }
}
