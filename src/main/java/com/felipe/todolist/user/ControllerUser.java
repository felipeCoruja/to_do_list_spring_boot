package com.felipe.todolist.user;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;





@RestController // implemeta na classe metodos de API-REST  
@RequestMapping("/users") // gerencia a rota da qual será acessada a classe via requisição 
public class ControllerUser {

    @Autowired// deixa o spring gerenciar o ciclo de vida da variavel, instancia por conta propria sem a necessidade de um construtor
    private InterfaceUserRepository userRepository;

    @PostMapping("/")
    //ResponseEntity é um objeto de retorno dinâmico para as requisições
    public ResponseEntity create(@RequestBody ModelUser usuario){// @RequestBody informa que o objeto ModelUser está chegando no body da requisição
        
        var user = this.userRepository.findByUserName(usuario.getUserName());
        if(user != null){
            System.out.println("Usuário já existe");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário já existe");
        }

        //passando a senha do usuário para o metodo de criptografia
        var passwordHashred = BCrypt.withDefaults().hashToString(12, usuario.getPassword().toCharArray());
        usuario.setPassword(passwordHashred);
        var userCreated = this.userRepository.save(usuario);//salvando no banco de dados com JPA
        return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
    }
    
    @PostMapping("/test")
    public String test(){
        System.out.println("Ta printando");
        String str = "Ta funcionando";
        return str;
    }
}
