package app.remoteservices;

public class ReturnMessage {
    boolean result;
    String message;

    public ReturnMessage(boolean result, String message) {
        this.result = result;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public boolean getResult() {
        return result;
    }
}
