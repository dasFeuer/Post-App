package com.example.barun.controllers;

import com.example.barun.entities.contactFormEntities.ContactForm;
import com.example.barun.services.ContactFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contactForm")
public class ContactFormControllers {

    @Autowired
    private ContactFormService formService;

    @Value("${contactFrom.recipient.email}")
    private String recipientEmail;

    @PostMapping("/sendForm")
    public String sendContactForm(@RequestBody ContactForm contactForm){
        String name = "Developer Barun";
        String subject = "You got a new Contact from! " + name;
        String body = "Name: " + contactForm.getFullName() + "\n" +
                      "Email: " + contactForm.getEmail() + "\n" +
                      "Message: " + contactForm.getMessage();

        formService.sendEmailOfContactForm(recipientEmail, subject, body, contactForm);
        return "Mail received";
    }

    @GetMapping("/allForms")
    public List<ContactForm> allContactForms(){
        return formService.getAllContactForm();
    }

    @DeleteMapping("/deleteAllForms")
    public ResponseEntity<Void> deleteAllTheForms(){
        formService.deleteAllContactForm();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
