package ass2.event;

import ass2.controller.Controller;
import ass2.model.classes.WikiLink;
import ass2.model.classes.mygraph.SimpleGraph;
import ass2.model.services.WikiClient;
import ass2.view.MainFrame;

import java.util.Set;
import io.vertx.core.*;

public class EventController implements Controller {
    private MainFrame view;
    private SimpleGraph graph;
    private Vertx vertx;

    public EventController() {

        // Generate the view.
        this.view = new MainFrame("Event programming", this);
        this.view.setVisible(true);

        this.vertx = Vertx.vertx();

        // Generate the model.
        //this.graph = new SimpleGraph(this);
    }

    @Override
    public void fetchConcept(String concept, int entry) {

        // Crea il grafo
        this.reset();

        //this.graph.addNode(concept);

        // Inizio della ricorsione
        this.startRecursion(concept, entry);

        //.onComplete(v -> log(v.result() + "Computation terminated."+ v.toString()));
        /*steps.onComplete(s-> {
            log("Result: " + s.getText());
            this.graphModel.addNode(s.getText());
            this.graphModel.addEdge(s.getConcept(), s.getText());
        });*/
    }

    @Override
    public void modelUpdated(String from) {
        this.view.display(from);
    }

    @Override
    public void modelUpdated(String from, String to) {
        this.view.display(from, to);
    }


    private void reset() { this.graph = new SimpleGraph(this);}

    private void  startRecursion(String concept, int entry) {

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


            Promise<Void> promise = Promise.promise();

            try {
                WikiClient client = new WikiClient();
                Set<WikiLink> set = client.parseURL(concept);
                set.forEach(element -> {
                    // Check if recursion needed.
                    if (entry > 1) {
                        //successivi al primi da 3 in poi
                        log("Recursion needed for " + element.getText() + " and " + entry);
                        startRecursion(element.getText(), entry - 1);
                    }
                });
            } catch (Exception e) {
                log("Exception in source: " + e.getMessage());
            }
            //return promise.future();

        }
    }

    private void log(String msg) {
        synchronized (System.out) {
            System.out.println("[" + Thread.currentThread().getName() + "]: " + msg);
        }
    }
}