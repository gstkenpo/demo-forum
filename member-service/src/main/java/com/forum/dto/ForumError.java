package com.forum.dto;

import java.util.Objects;

public class ForumError {
    private String field;
    private String errorMsg;

    public ForumError() {
    }

    public ForumError(String field, String errorMsg) {
        this.field = field;
        this.errorMsg = errorMsg;
    }

    public String getField() {
        return this.field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getErrorMsg() {
        return this.errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public ForumError field(String field) {
        this.field = field;
        return this;
    }

    public ForumError errorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof ForumError)) {
            return false;
        }
        ForumError forumError = (ForumError) o;
        return Objects.equals(field, forumError.field) && Objects.equals(errorMsg, forumError.errorMsg);
    }

    @Override
    public int hashCode() {
        return Objects.hash(field, errorMsg);
    }

    @Override
    public String toString() {
        return "{" +
            " field='" + getField() + "'" +
            ", errorMsg='" + getErrorMsg() + "'" +
            "}";
    }
    
}