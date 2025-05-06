package com.example.barun.domain;


public class CreateCommentRequest {

    private String comment;

    public CreateCommentRequest() {
    }

    public CreateCommentRequest(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}