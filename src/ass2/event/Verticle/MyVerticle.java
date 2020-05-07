package ass2.event.Verticle;

import ass2.model.classes.WikiLink;
import ass2.model.classes.mygraph.SimpleGraph;
import io.vertx.core.AbstractVerticle;
import ass2.model.services.WikiClient;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import sun.swing.plaf.synth.StyleAssociation;

import javax.sound.midi.SysexMessage;
import java.util.Set;

public class MyVerticle extends AbstractVerticle {

    private final WikiClient wikiClient = new WikiClient();

    public void start(Promise promise){
        //System.out.println("Prima o dopo della stampa del nuovo verticle??");
        promise.complete();
    }


    private void log(String msg) {
        synchronized (System.out) {
            System.out.println("[" + Thread.currentThread().getName() + "]: " + msg);
        }
    }


}
