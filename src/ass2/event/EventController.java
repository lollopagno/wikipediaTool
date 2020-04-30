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
    private SimpleGraph graphModel;

    public EventController() {
        // Generate the view.
        this.view = new MainFrame("Event programming", this);
        this.view.setVisible(true);

        // Generate the model.
        this.graphModel = new SimpleGraph(this);
    }

    @Override
    public void fetchConcept(String concept, int entry) {
        // log("Create source.");
        reset();
        //Vertx vertx = Vertx.vertx();
        this.graphModel.addNode(concept);
        log("prima");
        Future<Void> steps = fetchRecursivly(concept, entry)
                .onComplete(v -> log(v.result() + "Computation terminated."+ v.toString()));
        log("Dopo");
        steps.onComplete(s-> {
            log("Result: " + s.getText());
            this.graphModel.addNode(s.getText());
            this.graphModel.addEdge(s.getConcept(), s.getText());
        });
    }

    @Override
    public void modelUpdated(String from) {
        this.view.display(from);
    }

    @Override
    public void modelUpdated(String from, String to) {
        this.view.display(from, to);
    }

    public static void log(String msg) {
        synchronized (System.out) {
            System.out.println("[" + Thread.currentThread().getName() + "]: " + msg);
        }
    }

    private void reset() {
        this.graphModel = new SimpleGraph(this);
    }

    private Future<Void> /*void*/  fetchRecursivly(String concept, int entry) {
        Promise<Void> promise = Promise.promise();
        // Fetch Wikipedia.
        //prima volta che entra 2 ciclo
        try {
            WikiClient client = new WikiClient();
            Set<WikiLink> set = client.parseURL(concept);
            set.forEach(element -> {
                // Check if recursion needed.
                if (entry > 1) {
                    //successivi al primi da 3 in poi
                    log("Recursion needed for " + element.getText() + " and " + entry);
                    fetchRecursivly(element.getText(), entry - 1);
                }
            });
        } catch (Exception e) {
            log("Exception in source: " + e.getMessage());
        }
        return promise.future();
    }
}