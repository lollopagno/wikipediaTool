package ass2.executor;

import ass2.controller.Controller;
import ass2.model.classes.WikiLink;
import ass2.model.classes.mygraph.SimpleGraph;
import ass2.view.MainFrame;
import ass2.model.services.WikiClient;

import javax.swing.*;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ExecutorController implements Controller {
    private ExecutorService exec;
    public WikiClient wikiClient;
    public SimpleGraph graph;
    public MainFrame view;

    public ExecutorController(){

        // Crea la view
        this.view = new MainFrame("Executor programming", this);
        this.view.setVisible(true);

        // Oggetto per parse del URL
        this.wikiClient = new WikiClient();

    }

    public void reset() {
        this.graph = new SimpleGraph(this);
    }

    public void fetchConcept(String concept, int entry) {

        // Crea il grafo
        this.reset();

        // Inizia la ricorsione
        this.startRecursion(concept, entry);
    }

    @Override
    public void modelUpdated(String from) {
        SwingUtilities.invokeLater(() -> this.view.display(from));
    }

    @Override
    public void modelUpdated(String from, String to) {
        SwingUtilities.invokeLater(() -> this.view.display(from, to));
    }

    @Override
    public void displayNumber() {
        SwingUtilities.invokeLater(() -> this.view.displayNumber(this.graph.getNumberNode()));
    }

    // Parse del concetto e crea nuovi executor per le successive ricorsioni
    private void startRecursion(String concept, int entry) {

        // 1- Termina ricorsione
        if(entry == -1) {
            return;
        }

        // 2- Crea solo il primo nodo: entry == 0
        else if (entry == this.view.getEntryView()) {

            //Creo il primo vertice per il concetto dato in input nella view
            this.graph.addNode(concept);
            this.log("Ho aggiunto il nodo: " + concept );

        }

        // 3- Parse e ricorsione
        if (entry-1 != -1) {

            // Creo l'executor
            this.exec = Executors.newSingleThreadExecutor();

            exec.execute(() -> {

                try{

                    // Parse
                    Set<WikiLink> links = null;
                    try {
                        links = this.wikiClient.parseURL(concept);
                    } catch (Exception e) {
                        log(e.getMessage());
                    }

                    if (links == null) return;

                    // Ricorsione per ogni riferimento trovato
                    for (WikiLink elem : links) {

                        try {

                            //Creo il vertice per il nuovo concetto
                            this.graph.addNode(elem.getText());
                            this.log("Ho aggiunto il nodo: " + elem.getText());

                            try {

                                //Creo l'arco e aggancio il vertice al grafo
                                this.graph.addEdge(concept, elem.getText());

                                // Parto con la ricorsione
                                this.startRecursion(elem.getText(), entry - 1);

                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }

                        } catch (IllegalArgumentException e) {
                            this.log("Il concetto " + elem.getText() + " è già presente.");
                        }
                    }

                }catch(Exception e){
                    e.printStackTrace();
                }
            });
        }
    }

    private void log(String msg){
        synchronized (System.out)
        {
            System.out.println("[" + Thread.currentThread().getName() + "] " + msg);
        }
    }
}
