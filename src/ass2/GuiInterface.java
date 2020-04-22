package ass2;

import javax.swing.*;

public class GuiInterface {

    private String concept;
    private int level;

    public GuiInterface(){
        this.concept = "";
        this.level = 0;
    }

    public void setValue() {

        //Controllo che venga inserito un concetto
        while(this.concept.trim().equals("")) {
            // Memorizzo i dati inseriti dall'utente
            this.concept = JOptionPane.showInputDialog("Enter the concept");
        }

        // TODO fare verifica che l'utente digiti un numero e non una stringa
        try {
            this.level = Integer.parseInt(JOptionPane.showInputDialog("Enter the level"));
        }
        // in caso in cui non sia un numero
        catch (NumberFormatException ex){}

    }

    // Get del concetto da cercare su wiki
    public String getConcept(){
        return this.concept;
    }

    //Get dellla profondità desiderata
    public int getLevel(){
        return this.level;
    }
}
