package com.example.barun.domain;

public class PatchPostRequest {
    private String title;

    private String content;

    public PatchPostRequest() {
    }

    public PatchPostRequest(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
