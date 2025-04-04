package com.example.barun.repositories;

import com.example.barun.entities.contactFormEntities.ContactForm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactFormRepository extends JpaRepository<ContactForm ,Long> {

}
