package com.smartcontact2.dao;

import com.smartcontact2.entities.Contact;
import com.smartcontact2.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ContactRepository extends JpaRepository<Contact, Integer> {

    //currentPage-page
    //contact Per-page -5
    @Query("from Contact  as c where c.user.id=:userId")
    public Page<Contact> findContactByUser(@Param("userId") int userId, Pageable pageable);

    //search
    public List<Contact> findByNameContainingAndUser(String name, User user);
}
