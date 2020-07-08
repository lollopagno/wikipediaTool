package app.remoteservices;

import com.google.gson.annotations.SerializedName;

public class Box {
    @SerializedName("taken")
    private boolean taken;

    @SerializedName("originalPosition")
    private int originalPosition;

    @SerializedName("currentPosition")
    private int currentPosition;

    @SerializedName("taker")
    private String taker;

    @SerializedName("inRightPlace")
    private boolean inRightPlace;

    public Box(boolean taken, int originalPosition, int currentPosition, String taker, boolean inRightPlace){
        this.currentPosition = currentPosition;
        this.inRightPlace = inRightPlace;
        this.originalPosition = originalPosition;
        this.taken = taken;
        this.taker = taker;
    }

    public int getOriginalPosition() {
        return originalPosition;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public String getTaker() {
        return taker;
    }

}