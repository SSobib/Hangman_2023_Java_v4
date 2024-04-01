package controllers.listeners;

import models.Model;
import views.View;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class ButtonCancel implements ActionListener {
    private final Model model;
    private final View view;

    public ButtonCancel(Model model, View view) {
        this.model = model;
        this.view = view;
    }

    /**
     * The method that is executed when the cancel game button is clicked
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        // Clear guessed letters set
        ButtonSend.guessedLettersSet.clear();

        // Reset image id
        model.setImageId(0);

        // Set access to buttons and text field
        view.showNewButton();

        // Stop game time
        view.getGameTime().stopTimer();
        view.getGameTime().setRunning(false);

        // Start real time again
        view.getRealDateTime().start();

        // Reset result label
        view.getLblResult().setText("L E T ' S  P L A Y");
    }
}
