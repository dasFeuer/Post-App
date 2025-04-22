package com.example.barun.repositories;

import com.example.barun.domain.entities.ContactForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactFormRepository extends JpaRepository<ContactForm ,Long> {

}
