package com.example.barun.domain.dtos;


public class PostImageResponseDto {
    private String imageType;
    private String imageName;

    public PostImageResponseDto() {}

    public PostImageResponseDto(String imageType, String imageName) {
        this.imageType = imageType;
        this.imageName = imageName;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}
