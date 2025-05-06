package com.example.barun.domain.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateCommentRequestDto {
    @NotBlank(message = "Comment cannot be empty")
    @Size(min = 2, max = 200, message = "Comment must be between {min} and {max} characters")
    private String comment;

    public CreateCommentRequestDto() {
    }

    public CreateCommentRequestDto(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}