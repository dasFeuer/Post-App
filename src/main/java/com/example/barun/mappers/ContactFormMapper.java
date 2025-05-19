package com.example.barun.mappers;

import com.example.barun.domain.dtos.ContactFormDto;
import com.example.barun.domain.entities.ContactForm;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface ContactFormMapper {

    ContactFormDto toDto(ContactForm contactForm);

}
