package com.example.barun.mappers;

import com.example.barun.domain.dtos.CommentDto;
import com.example.barun.domain.entities.Comments;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface CommentMapper {

    @Mapping(target = "author", source = "author")
    @Mapping(target = "post", source = "post")
    CommentDto toDto(Comments comments);


}
