package ass2.event.Verticle;

import ass2.model.classes.WikiLink;
import ass2.model.classes.mygraph.SimpleGraph;
import io.vertx.core.AbstractVerticle;
import ass2.model.services.WikiClient;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import sun.swing.plaf.synth.StyleAssociation;

import java.util.Set;

public class MyVerticle extends AbstractVerticle {

    private final WikiClient wikiClient = new WikiClient();

    public void start(Promise promise){

        String concept = config().getString("concept");

        log("sono partito con "+concept);

        // Parse
        Set<WikiLink> links = null;
        try {
            links = this.wikiClient.parseURL(concept);
        } catch (Exception e) {
            promise.fail("error");
        }

        if (links == null) return;

        String text = "";
        for (WikiLink elem : links) {

            text += elem.getText()+"_";

        }

        promise.complete(text);
    }


    private void log(String msg) {
        synchronized (System.out) {
            System.out.println("[" + Thread.currentThread().getName() + "]: " + msg);
        }
    }


}
