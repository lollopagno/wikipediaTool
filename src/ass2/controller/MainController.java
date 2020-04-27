package ass2.controller;

import ass2.model.classes.WikiLink;
import ass2.model.classes.mygraph.SimpleGraph;
import ass2.view.View;
import ass2.model.services.WikiClient;

import javax.swing.*;
import java.util.Set;
import java.util.concurrent.Executors;

public class MainController implements Controller {
    public WikiClient wikiClient;
    public SimpleGraph graph;
    public View view;

    public MainController(View view){
        this.wikiClient = new WikiClient();
        this.view = view;
        this.reset();
    }

    public void reset() {
        this.graph = new SimpleGraph(this);
    }

    public void fetchConcept(String concept, int entry) {
        this.reset();
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

    private void startRecursion(String concept, int entry) {
        try {
            this.graph.addNode(concept);
        } catch (IllegalArgumentException e) {
            this.log("Illegal argument concept " + concept);
        }
        if(entry == 0) return; // Termina ricorsione.

        this.log(concept + " " + entry);
        try {
            Executors.newSingleThreadExecutor().execute(() -> {
                Set<WikiLink> links = null;
                try {
                    links = this.wikiClient.parseURL(concept);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if(links == null) return;

                for (WikiLink elem: links) {
                    // Entra nella ricorsione.
                    this.startRecursion(elem.getText(), entry - 1);
                    this.graph.addEdge(concept, elem.getText());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void log(String msg){
        synchronized (System.out)
        {
            System.out.println("[" + Thread.currentThread().getName() + "] " + msg);
        }
    }
}
