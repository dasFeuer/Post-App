package com.example.barun.services.impl;

import com.example.barun.domain.dtos.EmailBodyDto;
import com.example.barun.domain.entities.ContactForm;
import com.example.barun.repositories.ContactFormRepository;
import com.example.barun.services.ContactFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContactFormServiceImpl implements ContactFormService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private ContactFormRepository contactFormRepository;

   @Override
    public void sendEmailOfContactForm(EmailBodyDto emailBodyDto){
        try{
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(emailBodyDto.getTo());
            mailMessage.setSubject(emailBodyDto.getSubject());
            mailMessage.setText(emailBodyDto.getBody());
            javaMailSender.send(mailMessage);
            contactFormRepository.save(emailBodyDto.getContactForm());

        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
//    @Override
//    public void sendEmailOfContactForm(
//            String to, String subject, String body, ContactForm contactForm){
//        try{
//            SimpleMailMessage mailMessage = new SimpleMailMessage();
//            mailMessage.setTo(to);
//            mailMessage.setSubject(subject);
//            mailMessage.setText(body);
//            javaMailSender.send(mailMessage);
//            contactFormRepository.save(contactForm);
//
//        } catch (Exception e){
//            System.out.println(e.getMessage());
//        }
//    }

    @Override
    public List<ContactForm> getAllContactForm(){
        return contactFormRepository.findAll();
    }

    @Override
    public void deleteAllContactForm(){
        contactFormRepository.deleteAll();
    }

    @Override
    public Optional<ContactForm> getById(Long id){
        return contactFormRepository.findById(id);
    }

    @Override
    public void deleteById(Long id){
        contactFormRepository.deleteById(id);
    }
}
