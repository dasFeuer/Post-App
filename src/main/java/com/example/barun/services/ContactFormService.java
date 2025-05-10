package com.example.barun.services;

import com.example.barun.domain.dtos.EmailBodyDto;
import com.example.barun.domain.entities.ContactForm;

import java.util.List;
import java.util.Optional;

public interface ContactFormService {

//    void sendEmailOfContactForm(String to, String subject, String body, ContactForm contactForm);
    void sendEmailOfContactForm(EmailBodyDto emailBodyDto);
    List<ContactForm> getAllContactForm();
    void deleteAllContactForm();
    Optional<ContactForm> getById(Long id);
    void deleteById(Long id);
}
