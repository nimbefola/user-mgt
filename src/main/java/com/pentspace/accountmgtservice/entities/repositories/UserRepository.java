package com.pentspace.accountmgtservice.entities.repositories;

import com.pentspace.accountmgtservice.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findUserByEmail(String email);

}
