package ass2.model.classes;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class WikiLink {
    private int ns;
    private boolean exists;
    private String text, concept;

    public WikiLink(JsonElement element) {
        JsonObject elem = element.getAsJsonObject();
        this.ns = elem.get("ns").getAsInt();
        this.exists = elem.get("exists").getAsBoolean();
        this.text = elem.get("*").getAsString();
    }

    public WikiLink(JsonElement element, String concept) {
        this(element);
        this.concept = concept;
    }

    public String getText() {
        return text;
    }

    public String getConcept() {
        return concept;
    }
}
