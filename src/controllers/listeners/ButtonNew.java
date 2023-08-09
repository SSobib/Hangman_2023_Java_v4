package controllers.listeners;

import models.Model;
import views.View;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonNew implements ActionListener {
    private final Model model;
    private final View view;

    /**
     * New Game button constructor.
     * @param model Model
     * @param view View
     */
    public ButtonNew(Model model, View view) {
        this.model = model;
        this.view = view;
    }

    /**
     * Here is the action that happens when the New Game button is pressed
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        ButtonSend.guessedLettersSet.clear();
        model.setImageId(0);
        view.hideNewButtons();
        view.getRealDateTime().stop();
        if (!view.getGameTime().isRunning()) {
            view.getGameTime().setSeconds(0);
            view.getGameTime().setMinutes(0);
            view.getGameTime().setRunning(true);
            view.getGameTime().startTimer();
        } else {
            view.getGameTime().stopTimer();
            view.getGameTime().setRunning(false);
        }
        view.getTxtChar().requestFocus();
        view.setNewImage(0);
        String selectedCategory = view.getCmbCategory().getSelectedItem().toString();
        model.generatedWordFromCategoriesList(selectedCategory);
        String wordOfNew = model.spaceBetweenWords(String.valueOf(model.getNewWord()));
        view.getLblResult().setText(wordOfNew);
    }
}
