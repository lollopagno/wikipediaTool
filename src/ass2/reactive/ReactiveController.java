package ass2.reactive;

import ass2.controller.Controller;
import ass2.model.classes.WikiLink;
import ass2.model.classes.mygraph.SimpleGraph;
import ass2.model.services.WikiClient;
import ass2.view.MainFrame;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

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
        // log("Create source.");
        reset();
        this.graphModel.addNode(concept);
        Observable<WikiLink> source =
                Observable.create(emitter ->
                        new Thread(() -> {
                            // Access to wikipedia.
                            fetchRecursivly(concept, entry, emitter::onNext);
                            log("Computation terminated.");
                        }).start());
        // log("Created source.");
        // log("Subscribing source.");
        source
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .subscribe(s -> {
                            // Manage the new link.
                            log("Result: " + s.getText());
                            this.graphModel.addNode(s.getText());
                            this.graphModel.addEdge(s.getConcept(), s.getText());
                        }, (Throwable t) -> {
                    if(t instanceof IllegalArgumentException){
                        log("IllegalArgumentException thrown " + t.getMessage());
                    } else {
                        t.printStackTrace();
                    }
                        });
        // log("Subscribed source.");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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

    private void fetchRecursivly(String concept, int entry, Consumer<WikiLink> consumer) {
        // Fetch Wikipedia.
        try {
            WikiClient client = new WikiClient();
            Set<WikiLink> set = client.parseURL(concept);
            set.forEach(element -> {
                consumer.accept(element);

                // Check if recursion needed.
                if (entry > 1) {
                    log("Recursion needed for " + element.getText() + " and " + entry);
                    fetchRecursivly(element.getText(), entry - 1, consumer);
                }
            });
        } catch (Exception e) {
            log("Exception in source: " + e.getMessage());
        }
    }
}