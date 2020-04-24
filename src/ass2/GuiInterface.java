package ass2;

import javax.swing.*;

public class GuiInterface {

    private String concept;
    private int level;

    public GuiInterface(){
        this.concept = "";
        this.level = -1;
    }

    //Logica per settare un concetto dall'utente
    public void setConcept(){

        //Controllo che venga inserito un concetto
        while(this.concept.trim().equals("")) {
            // Memorizzo i dati inseriti dall'utente
            this.concept = JOptionPane.showInputDialog("Enter the concept");
        }
    }

    //Logica per settare il livello desiderato dall'utente
    public void setLevel(){

        //Controllo che venga inserito un numero e non una stringa
        while (this.level == -1 || this.level == 0) {
            try {
                this.level = Integer.parseInt(JOptionPane.showInputDialog("Enter the level > 0"));
            }
            // In caso in cui non sia un numero
            catch (NumberFormatException ex) {
                this.level = -1;
            }
        }
    }

    // Get del concetto da cercare su wiki
    public String getConcept(){
        return this.concept;
    }

    // Ritorna quante ricerche devo ancora effettuare
    public synchronized int getLevel(){
        return this.level;
    }

     //Aggiorno il livello di profondità
    public synchronized boolean updateLevel(){

        if (this.level > 0) {
            this.level -= 1;
            return true;
        }else{
            return false;
        }
    }
}
