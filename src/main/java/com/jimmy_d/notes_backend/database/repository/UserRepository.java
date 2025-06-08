package com.jimmy_d.notes_backend.database.repository;


import com.jimmy_d.notes_backend.database.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findOneByEmailOrUsername(String email, String username);
}
