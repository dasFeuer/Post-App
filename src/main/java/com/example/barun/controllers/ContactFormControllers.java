package com.example.barun.controllers;

import com.example.barun.entities.contactFormEntities.ContactForm;
import com.example.barun.services.ContactFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/contactForm")
public class ContactFormControllers {

    @Autowired
    private ContactFormService formService;

    @PostMapping("/sendForm")
    public String sendContactForm(@RequestBody ContactForm contactForm){
        String name = "Developer";
        String subject = "You got a new Contact from! " + name;
        String body = "Name: " + contactForm.getFullName() + "\n" +
                      "Email: " + contactForm.getEmail() + "\n" +
                      "Message: " + contactForm.getMessage();

        formService.sendEmailOfContactForm("sharanjan221@gmail.com", subject, body, contactForm);
        return "Mail received";
    }

}
