package model;

import enums.MarkType;
import java.io.Serializable;

public class Mark implements Serializable {
    private MarkType type;
    private double score;

    public Mark(MarkType type, double score) {
        this.type = type;
        this.score = score;
    }

    public MarkType getType() {
        return type;
    }

    public double getScore() {
        return score;
    }

    public void setType(MarkType type) {
        this.type = type;
    }

    public void setScore(double score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return type + ": " + score;
    }
}