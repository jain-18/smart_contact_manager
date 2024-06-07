package com.smartcontact2.dao;

import com.smartcontact2.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

@Component
public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("select u from User u  where u.email = :email ")
    public User getUserByUserName(@Param("email") String email);
}
