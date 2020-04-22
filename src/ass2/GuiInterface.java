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
        }catch (NumberFormatException ex){}
    }



    public String getConcept(){
        return this.concept;
    }

    public int getLevel(){
        return this.level;
    }
}
