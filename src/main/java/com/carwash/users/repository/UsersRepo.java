package com.carwash.users.repository;

import com.carwash.users.entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepo extends JpaRepository<UsersEntity,Long> {
    Optional<UsersEntity> findByEmail(String email); // Find user by email

}
