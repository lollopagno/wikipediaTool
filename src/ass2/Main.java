package ass2;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        //Gui
        GuiInterface gui = new GuiInterface();

        //Wikipedia Client
        wikipediaClient wc = new wikipediaClient(gui);

    }
}
