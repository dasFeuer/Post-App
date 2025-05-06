package com.example.barun.domain;


public class UpdateCommentRequest {

    private String comment;

    public UpdateCommentRequest() {
    }

    public UpdateCommentRequest(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}