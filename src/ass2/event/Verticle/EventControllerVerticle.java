package ass2.event.Verticle;

import ass2.controller.Controller;
import ass2.model.classes.WikiLink;
import ass2.model.classes.mygraph.SimpleGraph;
import ass2.model.services.WikiClient;
import ass2.view.MainFrame;

import java.util.Set;
import io.vertx.core.*;
import io.vertx.core.json.JsonObject;

import javax.swing.*;

public class EventControllerVerticle implements Controller {
    private MainFrame view;
    public WikiClient wikiClient;
    private SimpleGraph graph;
    private Vertx vertx;

    public EventControllerVerticle() {

        // Generate the view.
        this.view = new MainFrame("Event programming", this);
        this.view.setVisible(true);

        this.vertx = Vertx.vertx();
        this.wikiClient = new WikiClient();
        // Generate the model.
        //this.graph = new SimpleGraph(this);
    }

    @Override
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

    private void reset() {
        this.graph = new SimpleGraph(this);
    }


    private void startRecursion(String concept, int entry) {

        // 1- Termina ricorsione
        if (entry == -1) {
            return;

        // 2- Crea solo il primo nodo: entry == 0
        } else if (entry == this.view.getEntryView()) {

            // Creo il primo vertice per il concetto dato in input nella view
            this.graph.addNode(concept);
            this.log("Ho aggiunto il nodo: " + concept);
        }

        if (entry-1 != -1 ){

            //JsonObject config = new JsonObject().put("concept", concept);
            //DeploymentOptions options = new DeploymentOptions().setConfig(config);
            vertx.deployVerticle(new MyVerticle(), res -> {
                String deploymentID = res.result();
                System.out.println("Nuovo verticle: " + deploymentID);
                Set<WikiLink> links = null;
                try {
                    links = this.wikiClient.parseURL(concept);
                } catch (Exception e) {
                    log("error -- finish check");
                }
                if (links == null) return;
                if (res.succeeded()) {
                    System.out.println("daje");
                } else {
                    res.cause().printStackTrace();
                }
            });


        }
    }

    private void log(String msg) {
        synchronized (System.out) {
            System.out.println("[" + Thread.currentThread().getName() + "]: " + msg);
        }
    }
}