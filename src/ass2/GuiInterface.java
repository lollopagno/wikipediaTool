package ass2;

import javax.swing.*;

public class GuiInterface {

    private String concept;
    private int level;

    public GuiInterface(){

        // Memorizzo i dati inseriti dall'utente
        this.concept = JOptionPane.showInputDialog("Enter the concept");

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
