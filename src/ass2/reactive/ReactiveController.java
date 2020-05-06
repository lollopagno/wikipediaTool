package ass2.reactive;

import ass2.controller.Controller;
import ass2.model.classes.WikiLink;
import ass2.model.classes.mygraph.AssignmentGraph;
import ass2.model.classes.mygraph.SimpleGraph;
import ass2.model.services.WikiClient;
import ass2.view.MainFrame;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import javax.swing.*;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class ReactiveController implements Controller {
    private MainFrame view;
    private SimpleGraph graphModel;

    public ReactiveController() {
        // Generate the view.
        this.view = new MainFrame("Reactive programming", this);
        this.view.setVisible(true);

        // Generate the model.
        this.graphModel = new SimpleGraph(this);
    }

    @Override
    public void fetchConcept(String concept, int entry) {
        reset();
        this.graphModel.addNode(concept);
        Observable<WikiLink> source =
                Observable.create(emitter ->
                        new Thread(() -> {
                            // Access to wikipedia.
                            fetchRecursivly(concept, entry, emitter::onNext);
                            log("Computation terminated.");
                        }).start());

        source
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .subscribe(s -> {
                    // Manage the new link.
                    this.graphModel.addNode(s.getText());
                    this.graphModel.addEdge(s.getConcept(), s.getText());
                }, (Throwable t) -> {
                    if (t instanceof IllegalArgumentException) {
                        log("IllegalArgumentException thrown " + t.getMessage());
                    } else {
                        t.printStackTrace();
                    }
                });

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
        SwingUtilities.invokeLater(() -> this.view.displayNumber(this.graphModel.getNumberNode()));
    }

    public static void log(String msg) {
        synchronized (System.out) {
            System.out.println("[" + Thread.currentThread().getName() + "]: " + msg);
        }
    }

    private void reset() {
        this.graphModel = new SimpleGraph(this);
    }

    private void fetchRecursivly(String concept, int entry, Consumer<WikiLink> consumer) {
        // Fetch Wikipedia.
        WikiClient client = new WikiClient();

        Set<WikiLink> set = new HashSet<>();
        try {
            set = client.parseURL(concept);
        } catch (IOException ioException) {
            log("IOException in source: " + ioException.getMessage());
        } catch (Exception e) {
            log("Exception in source: " + e.getMessage());
            e.printStackTrace();
        }

        if (set == null || set.isEmpty())
            return;

        set.forEach(element -> {
            consumer.accept(element);

            // Check if recursion needed.
            if (entry > 1) {
                // log("Recursion needed for " + element.getText() + " and " + entry);
                fetchRecursivly(element.getText(), entry - 1, consumer);
            }
        });
    }
}