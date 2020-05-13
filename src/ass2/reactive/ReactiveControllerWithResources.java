package ass2.reactive;

import ass2.controller.Controller;
import ass2.model.classes.WikiLink;
import ass2.model.classes.mygraph.AssignmentGraph;
import ass2.model.classes.mygraph.NodeAlreadyPresent;
import ass2.model.classes.mygraph.SimpleGraph;
import ass2.model.services.WikiClient;
import ass2.view.MainFrame;
import ass2.view.ResourcesFrame;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import javax.swing.*;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class ReactiveControllerWithResources implements Controller {
    private final MainFrame view;
    private final ResourcesFrame resourcesView;
    private AssignmentGraph graphModel;
    private ExecutorService service;

    public ReactiveControllerWithResources() {
        // Generate the views.
        this.view = new MainFrame("Reactive Programming", this);
        this.view.setVisible(true);

        this.resourcesView = new ResourcesFrame();
        this.resourcesView.setVisible(true);

        // Generate the model.
        this.graphModel = new SimpleGraph(this);
    }

    public static void log(String msg) {
        synchronized (System.out) {
            System.out.println("[" + Thread.currentThread().getName() + "]: " + msg);
        }
    }

    private void reset() {
        this.graphModel = new SimpleGraph(this);
        int NTHREADS = Runtime.getRuntime().availableProcessors();
        this.service = Executors.newFixedThreadPool(NTHREADS);
    }

    @Override
    public void fetchConcept(String concept, int entry) {
        reset();
        this.graphModel.addNode(concept);
        this.generateEmitter(concept, entry);
    }

    public void generateEmitter(String concept, int entry) {
        Observable<WikiLink> source =
                Observable.create(emitter ->
                        this.service.execute(() -> {
                            // Access to wikipedia.
                            fetchRecursivly(concept, entry, emitter::onNext);
                        }));

        source
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .subscribe(s -> {
                    // Manage the new link.
                    try {
                        this.graphModel.addNode(s.getText());
                        this.graphModel.addEdge(s.getConcept(), s.getText());
                    } catch (NodeAlreadyPresent n) {
                        // log("Node already present " + n.getMessage());
                    } catch (Exception e) {
                        log("IllegalArgumentException thrown " + e.getMessage());
                    }
                }, (Throwable t) -> {
                    if (t instanceof IllegalArgumentException) {
                        log("IllegalArgumentException thrown " + t.getMessage());
                    } else {
                        t.printStackTrace();
                    }
                });
    }

    @Override
    public void modelUpdated(String from) {
        SwingUtilities.invokeLater(() -> {
            this.view.display(from);
            this.view.displayNumber(this.graphModel.getNodeNumber());
        });
    }

    @Override
    public void modelUpdated(String from, String to) {
        SwingUtilities.invokeLater(() -> {
            this.view.display(from, to);
            this.view.displayNumber(this.graphModel.getNodeNumber());
        });
    }

    private void fetchRecursivly(String concept, int entry, Consumer<WikiLink> consumer) {
        // Fetch Wikipedia.
        WikiClient client = new WikiClient();

        // Comunico alla view che c'è una risorsa in più da utilizzare.
        SwingUtilities.invokeLater(() -> this.resourcesView.putResources(1));

        Set<WikiLink> set = new HashSet<>();
        try {
            set = client.parseURL(concept);
        } catch (IOException ioException) {
            // log("IOException in source: " + ioException.getMessage());
        } catch (Exception e) {
            log("Exception in source: " + e.getMessage());
            e.printStackTrace();
        }

        // Comunico alla view che ho utilizzato una risorsa.
        SwingUtilities.invokeLater(() -> this.resourcesView.useResources(1));

        if (set == null || set.isEmpty())
            return;

        set.forEach(consumer);

        // Check if recursion needed.
        if (entry > 1) {
            set.forEach(elem -> generateEmitter(elem.getText(), entry - 1));
        }
    }
}