package com.example.buycation.mail.repository;

import com.example.buycation.mail.entity.EmailCheck;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailCheckRepository extends JpaRepository<EmailCheck, Long> {
    EmailCheck findByEmail(String email);
}
