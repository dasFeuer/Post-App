package com.example.barun.controllers;

import com.example.barun.domain.dtos.ContactFormDto;
import com.example.barun.domain.dtos.EmailBodyDto;
import com.example.barun.domain.entities.ContactForm;
import com.example.barun.mappers.ContactFormMapper;
import com.example.barun.services.impl.ContactFormServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/contactForm")
public class ContactFormControllers {

    @Autowired
    private ContactFormServiceImpl formService;

    @Autowired
    private ContactFormMapper contactFormMapper;

    @Value("${contactForm.recipient.email}")
    private String recipientEmail;

    @PostMapping("/sendForm")
    public String sendContactForm(@RequestBody ContactForm contactForm){
        EmailBodyDto emailBodyDto = new EmailBodyDto(
                recipientEmail,
                "You got a new message from: " + contactForm.getFullName(),
                "Name: " + contactForm.getFullName() + "\n" +
                        "Email: " + contactForm.getEmail() + "\n" +
                        "Message: " + contactForm.getMessage(),
                contactForm
        );
        formService.sendEmailOfContactForm(emailBodyDto);
        return "Mail received";
    }

    @GetMapping("/allForms")
    public List<ContactFormDto> allContactForms(){
        List<ContactForm> allContactForms = formService.getAllContactForm();
        if(allContactForms.isEmpty()){
            return List.of();
        }
        return allContactForms.
                stream().
                map(contactFormMapper::toDto).
                toList();
    }

    @DeleteMapping("/deleteAllForms")
    public ResponseEntity<Void> deleteAllTheForms(){
        formService.deleteAllContactForm();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{id}/form")
    public ResponseEntity<ContactFormDto> findById(@PathVariable Long id){
        Optional<ContactForm> byId = formService.getById(id);
        if(byId.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        ContactFormDto contactFormDto = contactFormMapper.toDto(byId.get());
        return ResponseEntity.ok(contactFormDto);
    }
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Void> deleteById(@PathVariable Long id){
        formService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
