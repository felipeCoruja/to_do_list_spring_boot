package com.felipe.todolist.user;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;



public interface InterfaceUserRepository extends JpaRepository<ModelUser,UUID> {
    
    ModelUser findByUserName (String userName);//o spring-data já entende com essa assinatura que ele precisa fazer um select
}
