package ass2.model.classes.mygraph;

public class NodeAlreadyPresent extends IllegalArgumentException {
    public NodeAlreadyPresent(String message){
        super(message);
    }
}
