package com.example.barun.domain.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UpdatePostRequestDto {
    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 200, message = "Title must be between {min } and {max} characters")
    private String title;

    @NotBlank(message = "Content is required")
    @Size(min = 10, max = 50000, message = "Content must be between {min } and {max} characters")
    private String content;

    public UpdatePostRequestDto() {
    }

    public UpdatePostRequestDto(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public @NotBlank(message = "Title is required")
    @Size(min = 3, max = 200, message = "Title must be between {min } and {max} characters") String getTitle() {
        return title;
    }

    public void setTitle(@NotBlank(message = "Title is required")
                         @Size(min = 3, max = 200, message = "Title must be between {min } and {max} characters") String title) {
        this.title = title;
    }

    public @NotBlank(message = "Content is required")
    @Size(min = 10, max = 50000, message = "Content must be between {min } and {max} characters") String getContent() {
        return content;
    }

    public void setContent(@NotBlank(message = "Content is required")
                           @Size(min = 10, max = 50000, message = "Content must be between {min } and {max} characters") String content) {
        this.content = content;
    }
}
