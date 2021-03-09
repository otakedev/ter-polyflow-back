package fr.polytech.webservices.models;

import java.util.Objects;

public class StepBody {
    private String comment;

    public StepBody() {
    }

    public StepBody(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public StepBody comment(String comment) {
        setComment(comment);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof StepBody)) {
            return false;
        }
        StepBody stepBody = (StepBody) o;
        return Objects.equals(comment, stepBody.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(comment);
    }

    @Override
    public String toString() {
        return "{" + " comment='" + getComment() + "'" + "}";
    }

}
