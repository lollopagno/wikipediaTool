package ass2;

public class Main {

    public static void main(String[] args) {

        //Gui
        GuiInterface gui = new GuiInterface();

        //Wikipedia Client
        wikipediaClient wc = new wikipediaClient(gui);

        try{
            wc.parseURL();
        }catch (Exception ex){}


    }
}
