package com.smart.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smart.entities.Contact;
import com.smart.entities.User;

public interface contactRepository extends JpaRepository<Contact, Integer>{

    @Query("from Contact as c where c.user.id = :userId")
  public  List<Contact> findContactsByUser(@Param("userId") int userId);
    
    //for search contact in show contact 
    public List<Contact> findByNameContainingIgnoreCaseAndUser(String name,User user );
}
