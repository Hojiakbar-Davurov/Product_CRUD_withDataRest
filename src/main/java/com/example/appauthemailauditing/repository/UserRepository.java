package com.example.appauthemailauditing.repository;

import com.example.appauthemailauditing.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.validation.constraints.Email;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByEmail(String email);
    Optional<User>findByEmail(@Email String email);
    Optional<User> findByEmailAndEmailCode(@Email String email, String emailCode);
}
