package com.felipe.todolist.task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.felipe.todolist.utils.Utils;

import jakarta.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/tasks")
public class ControllerTask {

    @Autowired
    private InterfaceTask repositoryTask;

    @PostMapping("/")
    public ResponseEntity create(@RequestBody ModelTask newTask, HttpServletRequest request){
        var idUser = (UUID) request.getAttribute("idUser");
        newTask.setUserId(idUser);

        var currentDate = LocalDateTime.now();

        if(currentDate.isAfter(newTask.getStartAt()) || currentDate.isAfter(newTask.getEndAt())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("A data de inicio e a data de término deve ser maior que a data atual");
        }else if(newTask.getStartAt().isAfter(newTask.getEndAt())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("A data de inicio deve ser maior que a data de término");
        }


        var taskSaved = repositoryTask.save(newTask);

        System.out.println("chegou no controller task");
        
        return ResponseEntity.status(HttpStatus.OK).body(taskSaved);
    }

    @GetMapping("/")
    public List<ModelTask> list(HttpServletRequest request){
        var idUser = (UUID) request.getAttribute("idUser");
        System.out.println("ID USER NA REQUEST>> " + idUser);
        var listTasks = this.repositoryTask.findByUserId(idUser);
        return listTasks;
    }
    
    //http://localhost:8080/tasks/23534534-sfdn23r234-235k45n25-nl24n234  id como parametro na URL
    @PutMapping("/{idTask}")//esse parâmetro é o que será passado para função abaixo via @PathVariable
    public ResponseEntity update(@RequestBody ModelTask taskModel, HttpServletRequest request, @PathVariable UUID idTask){
        
        var task = this.repositoryTask.findById((UUID) idTask).orElse(null);//está buscando uma task no banco, caso nao encontre retorna null
        if(task == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("Tarefa não encontrada");
        }

        var idUser = (UUID) request.getAttribute("idUser");

        //verificando se o usuário que quer alterar a task é o mesmo que a escreveu
        if(!task.getUserId().equals(idUser)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("Usuário não tem permissão para alterar essa tarefa");
        }
      
        Utils.copyNonNullProprieties(taskModel, task);
        var taskUpdated = this.repositoryTask.save(task);//o save() do JPA faz update caso o ID já esteja cadastrado
        return ResponseEntity.ok().body(taskUpdated);
    }
}
