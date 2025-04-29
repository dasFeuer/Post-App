package com.example.barun.domain.dtos;

public class AuthorDto {
    private Long id;
    private String fullName;

    public AuthorDto() {
    }

    public AuthorDto (Long id, String fullName) {
        this.id = id;
        this.fullName = fullName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }


}
