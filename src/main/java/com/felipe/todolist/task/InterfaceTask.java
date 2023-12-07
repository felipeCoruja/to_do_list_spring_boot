package com.felipe.todolist.task;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;


public interface InterfaceTask extends JpaRepository<ModelTask, UUID>{
    List<ModelTask> findByUserId(UUID userId);// o JPA já entende que deve retornar uma lista
}
