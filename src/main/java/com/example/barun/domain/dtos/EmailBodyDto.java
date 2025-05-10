package com.example.barun.domain.dtos;

import com.example.barun.domain.entities.ContactForm;
import jakarta.validation.constraints.NotBlank;

public class EmailBodyDto {
    @NotBlank(message = "Recipient email is required")
    private String to;

    @NotBlank(message = "Subject is required")
    private String subject;

    @NotBlank(message = "Body is required")
    private String body;

    @NotBlank(message = "Contact form is required")
    private ContactForm contactForm;

    public EmailBodyDto() {
    }

    public EmailBodyDto(String to, String subject, String body, ContactForm contactForm) {
        this.to = to;
        this.subject = subject;
        this.body = body;
        this.contactForm = contactForm;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public ContactForm getContactForm() {
        return contactForm;
    }

    public void setContactForm(ContactForm contactForm) {
        this.contactForm = contactForm;
    }
}
