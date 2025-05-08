package com.example.barun.mappers;

import com.example.barun.domain.dtos.PostLikeDto;
import com.example.barun.domain.entities.PostLike;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface PostLikeMapper {
    @Mapping(target = "user", source = "user")
    @Mapping(target = "post", source = "post")
    PostLikeDto toDto(PostLike postLike);
}
