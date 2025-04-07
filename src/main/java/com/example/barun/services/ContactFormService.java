package com.example.barun.services;

import com.example.barun.entities.contactFormEntities.ContactForm;
import com.example.barun.repositories.ContactFormRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContactFormService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private ContactFormRepository contactFormRepository;

    public void sendEmailOfContactForm(
            String to, String subject, String body, ContactForm contactForm){
        try{
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(to);
            mailMessage.setSubject(subject);
            mailMessage.setText(body);
            javaMailSender.send(mailMessage);
            contactFormRepository.save(contactForm);

        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public List<ContactForm> getAllContactForm(){
        return contactFormRepository.findAll();
    }

    public void deleteAllContactForm(){
        contactFormRepository.deleteAll();
    }

    public Optional<ContactForm> getById(Long id){
        return contactFormRepository.findById(id);
    }

    public void deleteById(Long id){
        contactFormRepository.deleteById(id);
    }
}
