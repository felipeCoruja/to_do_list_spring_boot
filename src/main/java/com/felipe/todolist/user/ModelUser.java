package com.felipe.todolist.user;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data //cria os getters e setters da minha classe pelo lombok
@Entity(name="tb_users")//usando o JPA para mapear a classe
public class ModelUser {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @Column(unique = true)
    private String userName;
    
    private String name;
    private String password;

    @CreationTimestamp
    private LocalDateTime createdAt;


  
}
