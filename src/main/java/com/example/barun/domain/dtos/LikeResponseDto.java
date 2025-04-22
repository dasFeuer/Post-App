package com.example.barun.domain.dtos;

public class LikeResponseDto {
    private boolean liked;
    private int likeCount;

    public LikeResponseDto () {}

    public LikeResponseDto(boolean liked, int likeCount) {
        this.liked = liked;
        this.likeCount = likeCount;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }
}
