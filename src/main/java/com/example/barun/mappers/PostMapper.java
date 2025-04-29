package com.example.barun.mappers;

import com.example.barun.domain.CreatePostRequest;
import com.example.barun.domain.UpdatePostRequest;
import com.example.barun.domain.dtos.CreatePostRequestDto;
import com.example.barun.domain.dtos.PostDto;
import com.example.barun.domain.dtos.UpdatePostRequestDto;
import com.example.barun.domain.entities.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface PostMapper {
    @Mapping(target = "author", source = "author")
    @Mapping(target = "comments", source = "comments")
    @Mapping(target = "likes", source = "likes")
    PostDto toDto(Post post);

    CreatePostRequest toCreatePostRequest(CreatePostRequestDto dto);

    UpdatePostRequest toUpdatePostRequest(UpdatePostRequestDto dto);

}
