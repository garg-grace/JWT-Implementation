package com.example.jwtdemo.user;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserAuth,Long> {
    Optional<UserAuth> findByUsername(String username);
}
